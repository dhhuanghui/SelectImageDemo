package com.tx.kotlinandroiddemo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


public class AlbumHelper1 {
    final String TAG = getClass().getSimpleName();
    //    Context context;
    private ContentResolver contentResolver;
    // 缩略图列表
    private LinkedHashMap<String, String> thumbnailList = new LinkedHashMap<String, String>();
    // 专辑列表
    private List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    private LinkedHashMap<String, ImageBucket1> bucketList = new LinkedHashMap<String, ImageBucket1>();
    private static AlbumHelper1 instance;

    private AlbumHelper1() {
    }

    public static AlbumHelper1 getHelper() {
        if (instance == null) {
            instance = new AlbumHelper1();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        contentResolver = context.getContentResolver();
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getThumbnailColumnData(cursor);
    }

    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur != null && cur.moveToFirst()) {
            @SuppressWarnings("unused")
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * 得到原图
     */
    void getAlbum() {
        String[] projection = {Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST,
                Albums.NUMBER_OF_SONGS};
        Cursor cursor = contentResolver.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getAlbumColumnData(cursor);
    }

    /**
     * 从本地数据库中得到原图
     *
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;
            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);
            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);
                // Do something with the values.
                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
                        + artist + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);
            } while (cur.moveToNext());
        }
    }

    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * 得到图片集
     */
    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();
        /* 初始化全部图片集合 */
        ImageBucket1 allImageBucket = new ImageBucket1();
        bucketList.put("-1000", allImageBucket);
        allImageBucket.imageList = new ArrayList<ImageItem1>();
        allImageBucket.bucketName = "所有图片";
        // 构造缩略图索引
        getThumbnail();
        // 构造相册索引
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME,
                Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        // 得到一个游标
        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " DESC");
//        new CursorLoader(activity,uri,projection,selection,selectionargs,sortorder)

        if (cur != null && cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            // 获取图片总数
//            int totalNum = cur.getCount();
            do {
                String _id = cur.getString(photoIDIndex);
//                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
//                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
//                String picasaId = cur.getString(picasaIdIndex);
                if ("0".equals(size) || path.endsWith(".gif")||!new File(path).exists()) {
                    continue;
                }
                ImageBucket1 bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket1();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem1>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem1 imageItem = new ImageItem1();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                bucket.imageList.add(imageItem);
                allImageBucket.count++;
                allImageBucket.imageList.add(imageItem);
            } while (cur.moveToNext());
        }
//        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
//        while (itr.hasNext()) {
//            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
//            ImageBucket bucket = entry.getValue();
//            for (int i = 0; i < bucket.imageList.size(); ++i) {
//                ImageItem image = bucket.imageList.get(i);
//                ELog.e(TAG, "----- " + bucket.bucketName + "--" + image.imagePath + "--" + image.thumbnailPath);
//            }
//        }
        hasBuildImagesBucketList = true;
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
    }
    /**
     * 得到视频集
     */
//    public ArrayList<VideoFolder> getVideoBucketList() {
//        ArrayList<VideoFolder> videoFolders = new ArrayList<>();   //所有的视频文件夹
//       String[] IMAGE_PROJECTION = {     //查询视频需要的数据列
//                MediaStore.Video.Media.DISPLAY_NAME,   //视频的显示名称  aaa.jpg
//                MediaStore.Video.Media.DATA,           //视频的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
//                MediaStore.Video.Media.SIZE,           //视频的大小，long型  132492
//                MediaStore.Video.Media.WIDTH,          //视频的宽度，int型  1920
//                MediaStore.Video.Media.HEIGHT,         //视频的高度，int型  1080
//                MediaStore.Video.Media.MIME_TYPE,      //视频的类型     image/jpeg
//                MediaStore.Video.Media.DATE_ADDED       //视频被添加的时间，long型  1450518608
//                , MediaStore.Video.Media.DURATION};    //视频的时长
//        // 得到一个游标
////        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " DESC");
////        new CursorLoader(activity,uri,projection,selection,selectionargs,sortorder)
//        Cursor data = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[7] + " DESC");
//        if (data != null) {
//            ArrayList<VideoItem> allVideos = new ArrayList<>();   //所有视频的集合,不分文件夹
//            while (data.moveToNext()) {
//                //查询数据
//                String videoName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//                String videoPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
//                long videoSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//                int videoWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
//                int videoHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
//                String videoMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
//                long videoAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
//                long videoTimeLong = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[7]));
//                //封装实体
//                VideoItem videoItem = new VideoItem();
//                videoItem.name = videoName;
//                videoItem.path = videoPath;
//                videoItem.size = videoSize;
//                videoItem.width = videoWidth;
//                videoItem.height = videoHeight;
//                videoItem.mimeType = videoMimeType;
//                videoItem.addTime = videoAddTime;
//                videoItem.timeLong = videoTimeLong;
//                allVideos.add(videoItem);
//                //根据父路径分类存放视频
//                //根据视频的路径获取到视频所在文件夹的路径和名称
//                File videoFile = new File(videoPath);
//                File videoParentFile = videoFile.getParentFile();
//                VideoFolder videoFolder = new VideoFolder();
//                videoFolder.name = videoParentFile.getName();
//                videoFolder.path = videoParentFile.getAbsolutePath();
//                //判断这个文件夹是否已经存在  如果存在直接添加视频进去  否则将文件夹添加到文件夹的集合中
//                if (!videoFolders.contains(videoFolder)) {
//                    ArrayList<VideoItem> images = new ArrayList<>();
//                    images.add(videoItem);
//                    //缩略图
//                    videoFolder.cover = videoItem;
//                    videoFolder.videos = images;
//                    videoFolders.add(videoFolder);
//                } else {
//                    videoFolders.get(videoFolders.indexOf(videoFolder)).videos.add(videoItem);
//                }
//            }
//            //防止没有视频报异常
//            if (data.getCount() > 0) {
//                //构造所有视频的集合
//                VideoFolder allVideosFolder = new VideoFolder();
//                allVideosFolder.name = "全部图片";
//                allVideosFolder.path = "/";
//                //把第一张设置缩略图
//                allVideosFolder.cover = allVideos.get(0);
//                allVideosFolder.videos = allVideos;
//                videoFolders.add(0, allVideosFolder);  //确保第一条是所有图片
//            }
//        }
//        return videoFolders;
//    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    public ArrayList<ImageBucket1> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        ArrayList<ImageBucket1> tmpList = new ArrayList<ImageBucket1>();
        Iterator<Entry<String, ImageBucket1>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket1> entry = (Entry<String, ImageBucket1>) itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }


    public void clear() {
        contentResolver = null;
    }

    /**
     * 得到原始图像路径
     *
     * @param image_id
     * @return
     */
    String getOriginalImagePath(String image_id) {
        String path = null;
        String[] projection = {Media._ID, Media.DATA};
        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        }
        return path;
    }
}
