package com.tx.kotlinandroiddemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tv = findViewById<TextView>(R.id.tv)
        tv.setOnClickListener({
            val intent = Intent(this@MainActivity, TakePicActivity::class.java)
            startActivityForResult(intent, 10)
        })
        imageView = findViewById<ImageView>(R.id.iv)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            var imagePath = data.getStringExtra("imagePath")
            Glide.with(this).load("file:///" + imagePath).into(imageView)
        }
    }

}
