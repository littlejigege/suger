package com.mobile.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

/**
 * Created by jimji on 2017/9/23.
 * @hide
 */
class EasyAdapter() : RecyclerView.Adapter<EasyAdapter.ViewHolder>() {
    companion object {
        val HEADER = Int.MAX_VALUE - 1
        val FOOTER = Int.MAX_VALUE - 2
    }

    private val mItemConfigs = mutableMapOf<Int, ItemConfig>()
    private val mDatas = mutableListOf<Any>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mDatas[position]
        mItemConfigs.map { entry ->
            if (entry.value.dataClass == data::class) {
                entry.value._bindData(data, holder)
            }
        }
    }

    override fun getItemCount(): Int = mDatas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val config = mItemConfigs[viewType]
        if (config != null) {
            val itemView = LayoutInflater.from(parent.context).inflate(config.layId, parent, false)
            val holder = ViewHolder(itemView)
            itemView.setOnClickListener {
                config._onClick(mDatas[holder.adapterPosition], holder.adapterPosition)
            }
            return holder
        }
        throw RuntimeException("viewType not found")
    }

    override fun getItemViewType(position: Int): Int {
        mItemConfigs.map { entry ->
            if (entry.value.dataClass == mDatas[position]::class) return entry.value.type
        }
        throw RuntimeException("viewType not found")
    }

    override fun onViewRecycled(holder: ViewHolder) {
        if (holder.adapterPosition == -1) {
            return
        }
        val data = mDatas[holder.adapterPosition]
        mItemConfigs.map {
            if (it.value.dataClass == data::class) {
                it.value._onRecycle(data, holder)
            }
        }
        super.onViewRecycled(holder)
    }

    fun addConfig(config: ItemConfig) {
        mItemConfigs.put(config.type, config)
    }

    fun addData(data: Any) {
        val lastIndex = if (mDatas.size == 0) 0 else mDatas.size - 1
        mDatas.add(data)
        notifyDataSetChanged()
    }

    fun addData(datas: List<Any>) {
        val lastIndex = if (mDatas.size == 0) 0 else mDatas.size - 1
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(index: Int, data: Any) {
        mDatas.add(index, data)
        notifyItemInserted(index)
    }

    fun addDataRange(indexStart: Int, datas: List<Any>) {
        mDatas.addAll(indexStart, datas)
        notifyItemRangeInserted(indexStart, datas.size)
    }

    fun update(newDatas: List<Any>) {
        mDatas.clear()
        mDatas.addAll(newDatas)
        notifyDataSetChanged()
    }

    fun removeData(index: Int) {
        if (mDatas.size == 0) return
        mDatas.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeData(range: IntRange) {
        if (mDatas.size - 1 < range.last || range.last < range.first) return
        range.forEach { mDatas.removeAt(range.first) }
        notifyItemRangeRemoved(range.first, range.last - range.first + 1)
    }

    fun clearData() {
        mDatas.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        //什么都可以，你想留着的东西
        lateinit var tmp: Any
    }

    open class ItemConfig(val ctx: Context, val dataClass: KClass<*>, setup: ItemConfig.() -> Unit) {
        companion object {
            val typeCount: AtomicInteger = AtomicInteger(0)
        }

        @LayoutRes
        var layId: Int = 0
        val type by lazy { typeCount.getAndIncrement() }
        var _bindData: (data: Any, holder: RecyclerView.ViewHolder) -> Unit = { _, _ -> }
        var _onClick: (data: Any, pos: Int) -> Unit = { _, _ -> }
        var _onRecycle: (Any, ViewHolder) -> Unit = { _, _ -> }
        fun onClick(c: (data: Any, pos: Int) -> Unit) {
            _onClick = c
        }

        fun onBindData(b: (data: Any, holder: RecyclerView.ViewHolder) -> Unit) {
            _bindData = b
        }

        fun onRecycle(r: (Any, ViewHolder) -> Unit) {
            _onRecycle = r
        }

        init {
            setup()
        }
    }


}