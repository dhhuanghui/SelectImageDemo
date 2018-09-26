package com.tx.kotlinandroiddemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class TakePicActivity : AppCompatActivity(), IOnItemClickListener<ImageItem> {
    override fun onItemClick(t: ImageItem) {
        var data = Intent()
        data.putExtra("imagePath", t.imagePath)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    var mDataList: ArrayList<ImageBucket>? = null// 相册文件夹集合

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_pic)
        var gridView = findViewById<GridView>(R.id.gridView_take_pic)
        AlbumHelper.init(this)
        mDataList = AlbumHelper.getImagesBucketList(false)
        val adapter = PictureAdapter(this, mDataList!!.get(0).imageList!!, this)
        gridView.adapter = adapter
    }
}
