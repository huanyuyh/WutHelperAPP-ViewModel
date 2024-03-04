package com.huanyu.hycnew.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.huanyu.hycnew.R
import com.huanyu.hycnew.activity.WebActivity
import com.huanyu.hycnew.entity.User

class UserListAdapter(mContext: Context, mUserList: List<User>): RecyclerView.Adapter<UserListViewHolder>() {
    var mUserList: List<User>
    var mContext: Context
    init {
        this.mUserList = mUserList
        this.mContext = mContext

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.textView.text = mUserList.get(position).platform
        holder.userEdit.setText(mUserList.get(position).name)
        holder.passEdit.setText(mUserList.get(position).pass)
        holder.userEdit.addTextChangedListener {
            mUserList.get(position).name = it.toString()
        }
        holder.passEdit.addTextChangedListener {
            mUserList.get(position).pass = it.toString()
        }
//        holder.bind(item)
    }

    override fun getItemCount() = mUserList.size
}


//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false)
//        val textView: TextView = view.findViewById(R.id.platformTv)
//        val userEdit: EditText = view.findViewById(R.id.usereditText)
//        val passEdit: EditText = view.findViewById(R.id.passeditText)
//        textView.text = mUserList.get(position).platform
//        userEdit.setText(mUserList.get(position).name)
//        passEdit.setText(mUserList.get(position).pass)
//        userEdit.addTextChangedListener {
//            mUserList.get(position).name = it.toString()
//        }
//        passEdit.addTextChangedListener {
//            mUserList.get(position).pass = it.toString()
//        }
//
//        return view
//    }
