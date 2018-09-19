package com.tx.kotlinandroiddemo

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

import java.util.ArrayList

/**
 * Created by dh on 2018/9/19.
 */

class PictureAdapter2(private val mContext: Activity, private val mCurrentThumbInfoList: ArrayList<ImageItem1>?, selectCount: Int) : BaseAdapter() {
    var selectCount = 0
        private set

    init {
        this.selectCount = selectCount
    }

    fun downSelectCount() {
        selectCount--
    }

    fun upSelectCount() {
        selectCount++
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.thumb_show_item, null)
            holder.ivImage = convertView!!.findViewById<View>(R.id.thumb_show_item_icon) as ImageView
            holder.ivSelected = convertView.findViewById<View>(R.id.iv_icon_data_selected) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val imageItem = mCurrentThumbInfoList!![position]
        if (imageItem.isSelected) {
            holder.ivSelected!!.visibility = View.VISIBLE
            holder.ivImage!!.setColorFilter(Color.parseColor("#50000000"))
        } else {
            holder.ivSelected!!.visibility = View.GONE
            holder.ivImage!!.clearColorFilter()
        }
        //        String path = ImageUtil.PREFIX_LOCAL + imageItem.imagePath;
        //        final ImageView imageView = holder.ivImage;
        //        imageView.setTag(path);
        //        ImageUtil.displayImage(mContext, ImageUtil.PREFIX_LOCAL + imageItem.imagePath, holder.ivImage);
        return convertView
    }

    override fun getCount(): Int {
        return mCurrentThumbInfoList?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return mCurrentThumbInfoList!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //    /**
    //     * 更新单条Item
    //     *
    //     * @param parent
    //     * @param pos
    //     * @param imageItem
    //     */
    //    public void updateItem(ViewGroup parent, int pos, final ImageItem imageItem) {
    //        UIViewUtil.updateGridView(parent, pos, -1, new OnItemUpdateListener() {
    //            @Override
    //            public void onUpdateCurrent(View view, int currentPos) {
    //                if (view.getTag() instanceof ViewHolder) {
    //                    ViewHolder holder = (ViewHolder) view.getTag();
    //                    if (imageItem.isSelected) {
    //                        if (mContext != null && mContext instanceof TakePictureActivity) {
    //                            ((TakePictureActivity) mContext).updateSelectList(imageItem);
    //                        }
    //                        holder.ivSelected.setVisibility(View.VISIBLE);
    //                        holder.ivImage.setColorFilter(Color.parseColor("#50000000"));
    //                    } else {
    //                        if (mContext != null && mContext instanceof TakePictureActivity) {
    //                            ((TakePictureActivity) mContext).updateSelectList(imageItem);
    //                        }
    //                        holder.ivSelected.setVisibility(View.GONE);
    //                        holder.ivImage.clearColorFilter();
    //                    }
    //                }
    //            }
    //
    //            @Override
    //            public void onUpdateOld(View view, int oldPosition) {
    //
    //            }
    //        });
    //    }

    inner class ViewHolder {
        var ivSelected: ImageView? = null
        var ivImage: ImageView? = null
    }

}
