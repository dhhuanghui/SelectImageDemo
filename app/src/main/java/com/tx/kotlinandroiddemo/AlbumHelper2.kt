//package com.tx.kotlinandroiddemo
//
//import android.content.ContentResolver
//import android.content.Context
//import android.database.Cursor
//import android.provider.MediaStore.Audio.Albums
//import android.provider.MediaStore.Images.Media
//import android.provider.MediaStore.Images.Thumbnails
//import android.util.Log
//
//
//import java.io.File
//import java.util.ArrayList
//import java.util.HashMap
//import java.util.LinkedHashMap
//import kotlin.collections.Map.Entry
//
//
//class AlbumHelper2 private constructor() {
//    internal val TAG = javaClass.simpleName
//    //    Context context;
//    private var contentResolver: ContentResolver? = null
//    // 缩略图列表
//    private val thumbnailList = LinkedHashMap<String, String>()
//    // 专辑列表
//    private val albumList = ArrayList<HashMap<String, String>>()
//    private val bucketList = LinkedHashMap<String, ImageBucket1>()
//
//    /**
//     * 是否创建了图片集
//     */
//    internal var hasBuildImagesBucketList = false
//
//    /**
//     * 初始化
//     *
//     * @param context
//     */
//    fun init(context: Context) {
//        contentResolver = context.contentResolver
//    }
//
//    /**
//     * 得到缩略图
//     */
//    private fun getThumbnail() {
//        val projection = arrayOf(Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA)
//        val cursor = contentResolver!!.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null)
//        getThumbnailColumnData(cursor)
//    }
//
//    /**
//     * 从数据库中得到缩略图
//     *
//     * @param cur
//     */
//    private fun getThumbnailColumnData(cur: Cursor?) {
//        if (cur != null && cur.moveToFirst()) {
//            var _id: Int
//            var image_id: Int
//            var image_path: String
//            val _idColumn = cur.getColumnIndex(Thumbnails._ID)
//            val image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID)
//            val dataColumn = cur.getColumnIndex(Thumbnails.DATA)
//            do {
//                _id = cur.getInt(_idColumn)
//                image_id = cur.getInt(image_idColumn)
//                image_path = cur.getString(dataColumn)
//                thumbnailList.put("" + image_id, image_path)
//            } while (cur.moveToNext())
//        }
//    }
//
//    /**
//     * 得到原图
//     */
//    internal fun getAlbum() {
//        val projection = arrayOf(Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS)
//        val cursor = contentResolver!!.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null)
//        getAlbumColumnData(cursor)
//    }
//
//    /**
//     * 从本地数据库中得到原图
//     *
//     * @param cur
//     */
//    private fun getAlbumColumnData(cur: Cursor?) {
//        if (cur!!.moveToFirst()) {
//            var _id: Int
//            var album: String
//            var albumArt: String
//            var albumKey: String
//            var artist: String
//            var numOfSongs: Int
//            val _idColumn = cur.getColumnIndex(Albums._ID)
//            val albumColumn = cur.getColumnIndex(Albums.ALBUM)
//            val albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART)
//            val albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY)
//            val artistColumn = cur.getColumnIndex(Albums.ARTIST)
//            val numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS)
//            do {
//                // Get the field values
//                _id = cur.getInt(_idColumn)
//                album = cur.getString(albumColumn)
//                albumArt = cur.getString(albumArtColumn)
//                albumKey = cur.getString(albumKeyColumn)
//                artist = cur.getString(artistColumn)
//                numOfSongs = cur.getInt(numOfSongsColumn)
//                // Do something with the values.
//                Log.i(TAG, _id.toString() + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
//                        + artist + " numOfSongs: " + numOfSongs + "---")
//                val hash = HashMap<String, String>()
//                hash.put("_id", _id.toString() + "")
//                hash.put("album", album)
//                hash.put("albumArt", albumArt)
//                hash.put("albumKey", albumKey)
//                hash.put("artist", artist)
//                hash.put("numOfSongs", numOfSongs.toString() + "")
//                albumList.add(hash)
//            } while (cur.moveToNext())
//        }
//    }
//
//    /**
//     * 得到图片集
//     */
//    internal fun buildImagesBucketList() {
//        val startTime = System.currentTimeMillis()
//        /* 初始化全部图片集合 */
//        val allImageBucket = ImageBucket1()
//        bucketList.put("-1000", allImageBucket)
//        allImageBucket.imageList = ArrayList()
//        allImageBucket.bucketName = "所有图片"
//        // 构造缩略图索引
//        getThumbnail()
//        // 构造相册索引
//        val columns = arrayOf(Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME)
//        // 得到一个游标
//        val cur = contentResolver!!.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " DESC")
//        //        new CursorLoader(activity,uri,projection,selection,selectionargs,sortorder)
//
//        if (cur != null && cur.moveToFirst()) {
//            // 获取指定列的索引
//            val photoIDIndex = cur.getColumnIndexOrThrow(Media._ID)
//            val photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA)
//            val photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME)
//            val photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE)
//            val photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE)
//            val bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME)
//            val bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID)
//            val picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID)
//            // 获取图片总数
//            //            int totalNum = cur.getCount();
//            do {
//                val _id = cur.getString(photoIDIndex)
//                //                String name = cur.getString(photoNameIndex);
//                val path = cur.getString(photoPathIndex)
//                //                String title = cur.getString(photoTitleIndex);
//                val size = cur.getString(photoSizeIndex)
//                val bucketName = cur.getString(bucketDisplayNameIndex)
//                val bucketId = cur.getString(bucketIdIndex)
//                //                String picasaId = cur.getString(picasaIdIndex);
//                if ("0" == size || path.endsWith(".gif") || !File(path).exists()) {
//                    continue
//                }
//                var bucket: ImageBucket1? = bucketList[bucketId]
//                if (bucket == null) {
//                    bucket = ImageBucket1()
//                    bucketList.put(bucketId, bucket)
//                    bucket.imageList = ArrayList()
//                    bucket.bucketName = bucketName
//                }
//                bucket.count++
//                val imageItem = ImageItem1()
//                imageItem.imageId = _id
//                imageItem.imagePath = path
//                imageItem.thumbnailPath = thumbnailList[_id]
//                bucket.imageList.add(imageItem)
//                allImageBucket.count++
//                allImageBucket.imageList.add(imageItem)
//            } while (cur.moveToNext())
//        }
//        //        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
//        //        while (itr.hasNext()) {
//        //            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
//        //            ImageBucket bucket = entry.getValue();
//        //            for (int i = 0; i < bucket.imageList.size(); ++i) {
//        //                ImageItem image = bucket.imageList.get(i);
//        //                ELog.e(TAG, "----- " + bucket.bucketName + "--" + image.imagePath + "--" + image.thumbnailPath);
//        //            }
//        //        }
//        hasBuildImagesBucketList = true
//        val endTime = System.currentTimeMillis()
//        Log.d(TAG, "use time: " + (endTime - startTime) + " ms")
//    }
//    /**
//     * 得到视频集
//     */
//    //    public ArrayList<VideoFolder> getVideoBucketList() {
//    //        ArrayList<VideoFolder> videoFolders = new ArrayList<>();   //所有的视频文件夹
//    //       String[] IMAGE_PROJECTION = {     //查询视频需要的数据列
//    //                MediaStore.Video.Media.DISPLAY_NAME,   //视频的显示名称  aaa.jpg
//    //                MediaStore.Video.Media.DATA,           //视频的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
//    //                MediaStore.Video.Media.SIZE,           //视频的大小，long型  132492
//    //                MediaStore.Video.Media.WIDTH,          //视频的宽度，int型  1920
//    //                MediaStore.Video.Media.HEIGHT,         //视频的高度，int型  1080
//    //                MediaStore.Video.Media.MIME_TYPE,      //视频的类型     image/jpeg
//    //                MediaStore.Video.Media.DATE_ADDED       //视频被添加的时间，long型  1450518608
//    //                , MediaStore.Video.Media.DURATION};    //视频的时长
//    //        // 得到一个游标
//    ////        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " DESC");
//    ////        new CursorLoader(activity,uri,projection,selection,selectionargs,sortorder)
//    //        Cursor data = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[7] + " DESC");
//    //        if (data != null) {
//    //            ArrayList<VideoItem> allVideos = new ArrayList<>();   //所有视频的集合,不分文件夹
//    //            while (data.moveToNext()) {
//    //                //查询数据
//    //                String videoName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//    //                String videoPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
//    //                long videoSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//    //                int videoWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
//    //                int videoHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
//    //                String videoMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
//    //                long videoAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
//    //                long videoTimeLong = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));
//    //                //封装实体
//    //                VideoItem videoItem = new VideoItem();
//    //                videoItem.name = videoName;
//    //                videoItem.path = videoPath;
//    //                videoItem.size = videoSize;
//    //                videoItem.width = videoWidth;
//    //                videoItem.height = videoHeight;
//    //                videoItem.mimeType = videoMimeType;
//    //                videoItem.addTime = videoAddTime;
//    //                videoItem.timeLong = videoTimeLong;
//    //                allVideos.add(videoItem);
//    //                //根据父路径分类存放视频
//    //                //根据视频的路径获取到视频所在文件夹的路径和名称
//    //                File videoFile = new File(videoPath);
//    //                File videoParentFile = videoFile.getParentFile();
//    //                VideoFolder videoFolder = new VideoFolder();
//    //                videoFolder.name = videoParentFile.getName();
//    //                videoFolder.path = videoParentFile.getAbsolutePath();
//    //                //判断这个文件夹是否已经存在  如果存在直接添加视频进去  否则将文件夹添加到文件夹的集合中
//    //                if (!videoFolders.contains(videoFolder)) {
//    //                    ArrayList<VideoItem> images = new ArrayList<>();
//    //                    images.add(videoItem);
//    //                    //缩略图
//    //                    videoFolder.cover = videoItem;
//    //                    videoFolder.videos = images;
//    //                    videoFolders.add(videoFolder);
//    //                } else {
//    //                    videoFolders.get(videoFolders.indexOf(videoFolder)).videos.add(videoItem);
//    //                }
//    //            }
//    //            //防止没有视频报异常
//    //            if (data.getCount() > 0) {
//    //                //构造所有视频的集合
//    //                VideoFolder allVideosFolder = new VideoFolder();
//    //                allVideosFolder.name = "全部图片";
//    //                allVideosFolder.path = "/";
//    //                //把第一张设置缩略图
//    //                allVideosFolder.cover = allVideos.get(0);
//    //                allVideosFolder.videos = allVideos;
//    //                videoFolders.add(0, allVideosFolder);  //确保第一条是所有图片
//    //            }
//    //        }
//    //        return videoFolders;
//    //    }
//
//    /**
//     * 得到图片集
//     *
//     * @param refresh
//     * @return
//     */
//    fun getImagesBucketList(refresh: Boolean): ArrayList<ImageBucket1> {
//        if (refresh || !refresh && !hasBuildImagesBucketList) {
//            buildImagesBucketList()
//        }
//        val tmpList = ArrayList<ImageBucket1>()
//        val itr = bucketList.entries.iterator()
//        while (itr.hasNext()) {
//            val entry = itr.next() as Entry<String, ImageBucket1>
//            tmpList.add(entry.value)
//        }
//        return tmpList
//    }
//
//
//    fun clear() {
//        contentResolver = null
//    }
//
//    /**
//     * 得到原始图像路径
//     *
//     * @param image_id
//     * @return
//     */
//    internal fun getOriginalImagePath(image_id: String): String? {
//        var path: String? = null
//        val projection = arrayOf(Media._ID, Media.DATA)
//        val cursor = contentResolver!!.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null)
//        if (cursor != null) {
//            cursor.moveToFirst()
//            path = cursor.getString(cursor.getColumnIndex(Media.DATA))
//        }
//        return path
//    }
//
//    companion object {
//        private var instance: AlbumHelper? = null
//
//        val helper: AlbumHelper
//            get() {
//                if (instance == null) {
//                    instance = AlbumHelper()
//                }
//                return instance
//            }
//    }
//}
