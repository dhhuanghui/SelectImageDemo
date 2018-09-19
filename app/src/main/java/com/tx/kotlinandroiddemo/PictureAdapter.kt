package com.tx.kotlinandroiddemo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by dh on 2018/9/19.
 */
class PictureAdapter(private val context: Context, private val mDataList: ArrayList<ImageItem>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.thumb_show_item, null)
            holder.ivImage = convertView!!.findViewById<View>(R.id.thumb_show_item_icon) as ImageView
            holder.ivSelected = convertView.findViewById<View>(R.id.iv_icon_data_selected) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val imageItem = mDataList[position]
        if (imageItem.isSelected) {
            holder.ivSelected!!.visibility = View.VISIBLE
            holder.ivImage!!.setColorFilter(Color.parseColor("#50000000"))
        } else {
            holder.ivSelected!!.visibility = View.GONE
            holder.ivImage!!.clearColorFilter()
        }
        Glide.with(context).load("file:///" + imageItem.imagePath).into(holder.ivImage)
        return convertView
    }

    override fun getItem(position: Int): Any? {
        return null
    }


    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    inner class ViewHolder {
        var ivSelected: ImageView? = null
        var ivImage: ImageView? = null
    }

}