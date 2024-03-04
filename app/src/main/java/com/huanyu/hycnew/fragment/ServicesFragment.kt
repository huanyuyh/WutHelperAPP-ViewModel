package com.huanyu.hycnew.fragment

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.R
import com.huanyu.hycnew.activity.UserActivity
import com.huanyu.hycnew.adapter.ServiceListAdapter
import com.huanyu.hycnew.database.UserDBHelper
import com.huanyu.hycnew.databinding.FragmentHomeBinding
import com.huanyu.hycnew.databinding.FragmentServicesBinding

class ServicesFragment : Fragment() {
    companion object {
        fun newInstance() = ServicesFragment()
    }
    lateinit var _binding: FragmentServicesBinding
    private lateinit var viewModel: ServicesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServicesBinding.inflate(layoutInflater, container, false)
        val platList = MyApplication.userDBHelper.queryAllPlatformInfo()
//        Log.d("data",platList.toString())
        //使用requireContext避免旋转重建时报错
        _binding.serviesList.adapter = ServiceListAdapter(mContext = requireContext(),platList)
        _binding.serviesList.stretchMode = GridView.STRETCH_COLUMN_WIDTH
        _binding.usersBtn.setOnClickListener {
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
        }
        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ServicesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}