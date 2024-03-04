package com.huanyu.hycnew.adapter

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPageAdapter(fragmentList:List<Fragment>,fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity) {
    private lateinit var fragmentList:List<Fragment>
    init {
        this.fragmentList = fragmentList
    }
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }
}