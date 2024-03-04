package com.huanyu.hycnew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.huanyu.hycnew.R

class WeekAdapter(context: Context, weekList:ArrayList<String>):BaseAdapter() {
    private var weekList:List<String>
    private var context:Context
    init {
        this.context = context
        this.weekList = weekList
    }
    override fun getCount(): Int {
        return weekList.size

    }

    override fun getItem(position: Int): String {
        return weekList.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_week, parent, false)
        val textView: TextView = view.findViewById(R.id.weekTv)
        textView.text = weekList.get(position)
        return view
    }
}