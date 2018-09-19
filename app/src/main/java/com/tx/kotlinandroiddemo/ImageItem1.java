package com.tx.kotlinandroiddemo;

import java.io.Serializable;

/**
 * 一个图片对象
 *
 * @author huanghui
 */
public class ImageItem1 implements Serializable {
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;

}
