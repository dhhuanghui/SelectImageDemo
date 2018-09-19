package com.tx.kotlinandroiddemo

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import java.io.File

/**
 * Created by dh on 2018/9/19.
 */
object AlbumHelper {
    private var contentResolver: ContentResolver? = null
    private val thumbnailList = LinkedHashMap<String, String>()
    private val albumList = ArrayList<HashMap<String, String>>()
    private val bucketList = LinkedHashMap<String, ImageBucket>()

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context) {
        contentResolver = context.contentResolver
    }

    /**
     * 得到缩略图
     */
    fun getThumbnail() {
        var projection = arrayOf(MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA)
        var cursor = contentResolver!!.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null)
        getThumbnailColumnData(cursor)
    }


    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    fun getThumbnailColumnData(cur: Cursor) {
        if (cur != null && cur.moveToFirst()) {
            var _id: Int
            var image_id: Int
            var image_path: String
            var _idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails._ID)
            var image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID)
            var dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA)
            do {
                _id = cur.getInt(_idColumn)
                image_id = cur.getInt(image_idColumn)
                image_path = cur.getString(dataColumn)
                thumbnailList.put(image_id.toString(), image_path)
            } while (cur.moveToNext())
        }

    }
//
    /**
     * 得到原图
     */
    private fun getAlbum() {
        var projection = arrayOf(
                MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ALBUM_KEY, MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS)
        var cursor = contentResolver!!.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, null)
        getAlbumColumnData(cursor)
    }
//
    /**
     * 从本地数据库中得到原图
     *
     * @param cur
     */
    private fun getAlbumColumnData(cur: Cursor) {
        if (cur.moveToFirst()) {
            if (cur.moveToFirst()) {
                var _id: Int
                var album: String
                var albumArt: String
                var albumKey: String
                var artist: String
                var numOfSongs: Int
                var _idColumn = cur.getColumnIndex(MediaStore.Audio.Albums._ID)
                var albumColumn = cur.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
                var albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
                var albumKeyColumn = cur.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY)
                var artistColumn = cur.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
                var numOfSongsColumn = cur.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
                do {
                    // Get the field values
                    _id = cur.getInt(_idColumn)
                    album = cur.getString(albumColumn)
                    albumArt = cur.getString(albumArtColumn)
                    albumKey = cur.getString(albumKeyColumn)
                    artist = cur.getString(artistColumn)
                    numOfSongs = cur.getInt(numOfSongsColumn)
                    // Do something with the values.
                    Log.i("dh", _id.toString() + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
                            + artist + " numOfSongs: " + numOfSongs + "---")
                    var hash = HashMap<String, String>()
                    hash.put("_id", _id.toString())
                    hash.put("album", album);
                    hash.put("albumArt", albumArt);
                    hash.put("albumKey", albumKey);
                    hash.put("artist", artist);
                    hash.put("numOfSongs", numOfSongs.toString());
                    albumList.add(hash);
                } while (cur.moveToNext());
            }
        }

    }

    /**
     * 是否创建了图片集
     */
    var hasBuildImagesBucketList: Boolean = false

    /**
     * 得到图片集
     */
    fun buildImagesBucketList() {
        var startTime = System.currentTimeMillis()
        /* 初始化全部图片集合 */
        var allImageBucket = ImageBucket()
        bucketList.put("-1000", allImageBucket)
        allImageBucket.imageList = ArrayList<ImageItem>()
        allImageBucket.bucketName = "所有图片"
        // 构造缩略图索引
        getThumbnail()
        // 构造相册索引
        var columns = arrayOf(
                MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        // 得到一个游标
        var cur = contentResolver!!.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
//        new CursorLoader(activity,uri,projection,selection,selectionargs,sortorder)

        if (cur != null && cur.moveToFirst()) {
            // 获取指定列的索引
            var photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            var photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            var photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            var photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            var photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            var bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            var bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            var picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
            // 获取图片总数
//            int totalNum = cur.getCount();
            do {
                var _id = cur.getString(photoIDIndex);
//                String name = cur.getString(photoNameIndex);
                var path = cur.getString(photoPathIndex);
//                String title = cur.getString(photoTitleIndex);
                var size = cur.getString(photoSizeIndex)
                var bucketName = cur.getString(bucketDisplayNameIndex)
                var bucketId = cur.getString(bucketIdIndex);
                if ("0".equals(size) || path.endsWith(".gif") || !File(path).exists()) {
                    continue
                }
                var bucket = bucketList[bucketId]
                if (bucket == null) {
                    bucket = ImageBucket()
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = ArrayList<ImageItem>()
                    bucket.bucketName = bucketName;
                }
                bucket.count++
                var imageItem = ImageItem()
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbnailPath = thumbnailList[_id]
                bucket.imageList!!.add(imageItem)
                allImageBucket.count++
                allImageBucket.imageList!!.add(imageItem);
            } while (cur.moveToNext());
        }
        hasBuildImagesBucketList = true;
        var endTime = System.currentTimeMillis();
        Log.d("dh", "use time: " + (endTime - startTime) + " ms");
    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    fun getImagesBucketList(refresh: Boolean): ArrayList<ImageBucket> {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        var tmpList = ArrayList<ImageBucket>()
        var itr = bucketList.entries.iterator()
        while (itr.hasNext()) {
            var entry = itr.next() as kotlin.collections.Map.Entry<String, ImageBucket>
            tmpList.add(entry.value)
        }
        return tmpList;
    }

    //
//
    fun clear() {
        contentResolver = null;
    }
//
    /**
     * 得到原始图像路径
     *
     * @param image_id
     * @return
     */
    fun getOriginalImagePath(image_id: String): String? {
        var path: String? = null
        var projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        var cursor = contentResolver!!.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        return path
    }
}