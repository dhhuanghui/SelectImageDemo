package com.tx.kotlinandroiddemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tv = findViewById<TextView>(R.id.tv)
        tv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, TakePicActivity::class.java)
            startActivity(intent)
        })
    }
}
