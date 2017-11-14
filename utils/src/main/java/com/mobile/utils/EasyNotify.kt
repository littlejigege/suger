package com.mobile.utils

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * Created by steve on 17-11-14.
 */
fun <T> AutoNotifyBaseAdapter<*, *>.autoNotify(oldList: List<T>, newList: List<T>, compare: (T, T) -> Boolean) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    })

    diff.dispatchUpdatesTo(this)
}

abstract class AutoNotifyBaseAdapter <T,VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun getDataSource() : MutableList<T>

    //Plase copy the data in it initial
    var oldDatas : MutableList<T> = mutableListOf()

    //判断两项是否是同一项
    fun easyNotify(compare : (T, T) -> Boolean){
        val newDatas = getDataSource()
        autoNotify(oldDatas, newDatas, compare)
        oldDatas.clear()
        oldDatas.addAll(newDatas)
    }
}

abstract class AutoNotifyAdapter <T,VH : RecyclerView.ViewHolder> (val source : MutableList<T>) : AutoNotifyBaseAdapter<T, VH>() {

    override fun getDataSource(): MutableList<T> {
        return source
    }

    //copy that
    init{
        oldDatas.addAll(source)
    }

}

