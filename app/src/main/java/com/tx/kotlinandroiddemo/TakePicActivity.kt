package com.tx.kotlinandroiddemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class TakePicActivity : AppCompatActivity() {
    var mDataList: ArrayList<ImageBucket>? = null// 相册文件夹集合

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_pic)
        var gridView = findViewById<GridView>(R.id.gridView_take_pic)
        AlbumHelper.init(this)
        mDataList = AlbumHelper.getImagesBucketList(false)
        val adapter = PictureAdapter(this, mDataList!!.get(0).imageList!!)
        gridView.adapter = adapter
    }
}
