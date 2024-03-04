package com.huanyu.hycnew

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.huanyu.hycnew.activity.UserActivity
import com.huanyu.hycnew.activity.WebActivity
import com.huanyu.hycnew.adapter.MyFragmentPageAdapter
import com.huanyu.hycnew.databinding.ActivityMainBinding
import com.huanyu.hycnew.fragment.HomeFragment
import com.huanyu.hycnew.fragment.NotifiFragment
import com.huanyu.hycnew.fragment.ServicesFragment
import com.huanyu.hycnew.fragment.TableFragment

class MainActivity : AppCompatActivity() {
    lateinit var _binding:ActivityMainBinding

    fun selectPage(item:Int){
        _binding.mainPager.setCurrentItem(item,true)
    }
    fun selectItem(item: Int){
        _binding.leftNavi?.menu?.getItem(item)?.isChecked = true
        _binding.bottomNavi?.menu?.getItem(item)?.isChecked = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _binding.bottomNavi?.setOnItemSelectedListener {
            selectPage(it.order)
            true
        }
        _binding.leftNavi?.setNavigationItemSelectedListener {
            selectPage(it.order)
            true
        }
        var fragmentlist = mutableListOf<Fragment>(
            TableFragment.newInstance(),
            HomeFragment.newInstance(),
            NotifiFragment.newInstance(),
            ServicesFragment.newInstance(),


        )
        _binding.mainPager.adapter = MyFragmentPageAdapter(fragmentlist,this)
        _binding.mainPager.offscreenPageLimit = 4
        _binding.mainPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(position==0||position==2){
                    _binding.mainPager.isUserInputEnabled = false
                }else{
                    _binding.mainPager.isUserInputEnabled = true
                }
                selectItem(position)
                super.onPageSelected(position)
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}