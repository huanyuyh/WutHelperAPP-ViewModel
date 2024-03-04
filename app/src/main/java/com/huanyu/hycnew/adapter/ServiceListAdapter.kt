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

class ServiceListAdapter(mContext: Context, mPlatList: List<Platform>): BaseAdapter() {
    private var mContext: Context
    private var mPlatList: List<Platform>

    init {
        this.mPlatList = mPlatList
        this.mContext = mContext
    }
    override fun getCount(): Int {
        return mPlatList.size

    }

    override fun getItem(position: Int): Platform {
        return mPlatList.get(position)

    }

    override fun getItemId(position: Int): Long {
        return mPlatList.get(position)._id as Long

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false)
        val cardView: CardView = view.findViewById(R.id.passcardView)
        val textView: TextView = view.findViewById(R.id.platformTv)
        textView.text = mPlatList.get(position).platName
        cardView.setOnClickListener {
            val intent = Intent(mContext, WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString("platform",mPlatList.get(position).platName?:"智慧理工大")
            bundle.putString("action","open")
            intent.putExtra("bundle",bundle)
            mContext.startActivity(intent)
        }
        return view
    }

}