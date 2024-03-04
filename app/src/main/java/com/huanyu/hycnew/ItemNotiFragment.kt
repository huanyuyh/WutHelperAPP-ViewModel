package com.huanyu.hycnew

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.huanyu.hycnew.databinding.FragmentItemNotiBinding

class ItemNotiFragment : Fragment() {

    companion object {
        fun newInstance() = ItemNotiFragment()
    }
    lateinit var _binding:FragmentItemNotiBinding
    private lateinit var viewModel: ItemNotiViewModel
    var ImageUrl:String = "http://bjb.yunwj.top/php/tp/60.jpg"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemNotiBinding.inflate(layoutInflater ,container, false)
        val width = getResources().getDisplayMetrics().widthPixels
        Glide.with(requireContext())
            .load(ImageUrl)
            .override(width)
            .placeholder(R.drawable.baseline_downloading_24)//图片加载出来前，显示的图片
            .error(R.drawable.baseline_error_outline_24)//图片加载失败后，显示的图片
            .into(_binding.notiImageView);
        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ItemNotiViewModel::class.java)
        // TODO: Use the ViewModel
    }

}