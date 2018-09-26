package com.tx.kotlinandroiddemo

/**
 * Created by dh on 2018/9/19.
 */
interface IOnItemClickListener<in T> {
    fun onItemClick(t: T)
}