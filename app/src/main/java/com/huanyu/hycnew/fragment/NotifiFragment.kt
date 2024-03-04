package com.huanyu.hycnew.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huanyu.hycnew.ItemNotiFragment
import com.huanyu.hycnew.R
import com.huanyu.hycnew.adapter.MyFragmentPageAdapter
import com.huanyu.hycnew.databinding.FragmentHomeBinding
import com.huanyu.hycnew.databinding.FragmentNotifiBinding

class NotifiFragment : Fragment() {

    companion object {
        fun newInstance() = NotifiFragment()
    }
    lateinit var _binding:FragmentNotifiBinding
    private lateinit var viewModel: NotifiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotifiBinding.inflate(layoutInflater, container, false)
        var _60sFragment = ItemNotiFragment()
        _60sFragment.ImageUrl = "http://bjb.yunwj.top/php/tp/60.jpg"
        var _2Fragment = ItemNotiFragment()
        _2Fragment.ImageUrl = "https://xiaoapi.cn/API/lssdjt_pic.php"
        var zaFragment = ItemNotiFragment()
        zaFragment.ImageUrl = "https://api.lolimi.cn/API/image-zw/"
        var z60Fragment = ItemNotiFragment()
        z60Fragment.ImageUrl = "https://zj.v.api.aa1.cn/api/60s/"
        var o60Fragment = ItemNotiFragment()
        o60Fragment.ImageUrl = "https://zj.v.api.aa1.cn/api/60s-old/"
        var a60Fragment = ItemNotiFragment()
        a60Fragment.ImageUrl = "https://zj.v.api.aa1.cn/api/60s-v2/"
        var b60Fragment = ItemNotiFragment()
        b60Fragment.ImageUrl = "https://v.api.aa1.cn/api/60s-v3/"
        var c60Fragment = ItemNotiFragment()
        c60Fragment.ImageUrl = "https://www.apii.cn/api/60s-v5/"
        var fragmentList = mutableListOf<Fragment>(
            _60sFragment,
            _2Fragment,
            zaFragment,
            z60Fragment,
            o60Fragment,
            a60Fragment,
            b60Fragment,
            c60Fragment
        )
        _binding.notiViewPage.adapter = MyFragmentPageAdapter(fragmentList,requireActivity())
        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotifiViewModel::class.java)
    }

}