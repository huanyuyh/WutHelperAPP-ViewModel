package com.huanyu.hycnew.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.huanyu.hycnew.R
import com.huanyu.hycnew.activity.WebActivity
import com.huanyu.hycnew.entity.Platform

class AreaListAdapter(mContext: Context, mPlatList: List<String>): BaseAdapter() {
    private var mContext: Context
    private var mPlatList: List<String>

    init {
        this.mPlatList = mPlatList
        this.mContext = mContext
    }
    override fun getCount(): Int {
        return mPlatList.size

    }

    override fun getItem(position: Int): String {
        return mPlatList.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_spinner_area, parent, false)
        val textView: TextView = view.findViewById(R.id.platformTv)
        textView.text = mPlatList.get(position)
        return view
    }

}