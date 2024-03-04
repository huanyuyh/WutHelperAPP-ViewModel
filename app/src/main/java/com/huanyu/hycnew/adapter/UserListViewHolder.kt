package com.huanyu.hycnew.adapter

import android.content.ClipData
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huanyu.hycnew.R
import com.huanyu.hycnew.entity.User

class UserListViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.platformTv)
    val userEdit: EditText = view.findViewById(R.id.usereditText)
    val passEdit: EditText = view.findViewById(R.id.passeditText)

//    fun bind(mUser: User) {
//        textView.text = mUser.platform
//        userEdit.setText(mUser.name)
//        passEdit.setText(mUser.pass)
//    }
}