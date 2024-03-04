package com.huanyu.hycnew.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson2.JSONObject
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.R
import com.huanyu.hycnew.activity.WebActivity
import com.huanyu.hycnew.adapter.AreaListAdapter
import com.huanyu.hycnew.databinding.FragmentHomeBinding
import com.huanyu.hycnew.entity.Building
import com.huanyu.hycnew.utils.HttpTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class HomeFragment() : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }
    var areaLists = MyApplication.buildDBHelper.queryAllbuildingInfo()
    lateinit var myBroadcastReceiver: MyBroadcastReceiver
    lateinit var _binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var areaId:String
    lateinit var buildId:String
    class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // 处理接收到的广播
            val message = intent.getStringExtra("message")
            message?.let {
                if(message.contains("yes")) {



                }
            }


        }
    }

    fun setAreaSpin(list:MutableList<String>){
        _binding.AreaSpin.adapter = AreaListAdapter(requireContext(),list)
        _binding.AreaSpin.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {

                var area = MyApplication.buildDBHelper.queryBuildingIdInfoByArea(list.get(i))
                areaId = area.areaId
                GlobalScope.launch(Dispatchers.IO) {
                    var respon = HttpTools.DianfeiGet(
                        "Build",
                        MyApplication.JFPTcookie,
                        "&areaid=" + area.areaId
                    )
                    if(respon.contains("buildList")){
                        Log.d("responbuild", respon)
                        var jsonObject = JSONObject.parseObject(respon)

                        var areas = HttpTools.convertStringToMap(jsonObject.getString("buildList"))
                        Log.d("responbuild", area.areaId)

                        var newlist:MutableList<Building> = mutableListOf()
                        val areaList = mutableListOf<String>()
                        areas.forEach{
                            newlist.add( Building(0,list.get(i),it.value,it.key))
                            areaList.add(it.value)
                        }
                        MyApplication.buildDBHelper.deleteBuildingInfoByParent(list.get(i))
                        MyApplication.buildDBHelper.insertBuildingInfoList(newlist)
                        withContext(Dispatchers.Main){
                            setBuildSpin(areaList)
                        }
                    }

                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }
    }

    fun setBuildSpin(list:MutableList<String>) {
        _binding.BuildSpin.adapter =
            AreaListAdapter(requireContext(), list)
        _binding.BuildSpin.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    Log.d("build",list.get(i))
                    var area = MyApplication.buildDBHelper.queryBuildingIdInfoByArea(list.get(i))
                    buildId = area.areaId
                    Log.d("responbuild", area.toString())
                    GlobalScope.launch(Dispatchers.IO) {
                        var respon = HttpTools.DianfeiGet(
                            "Floor",
                            MyApplication.JFPTcookie,
                            "&areaid=" + areaId + "&buildid=" + area.areaId
                        )
                        Log.d("data", "&areaid=" + areaId + "&buildid=" + area.areaId)
                        Log.d("responbuild", respon)
                        if(respon.contains("floorList")){
                            var jsonObject = JSONObject.parseObject(respon)
                            var floorString:String =jsonObject.getString("floorList")
                            floorString = floorString.substring(1, floorString.length - 1);
                            var strArray = floorString.split(",");
                            var newlist:MutableList<Building> = mutableListOf()
                            val areaList = mutableListOf<String>()
                            strArray.forEach{
                                newlist.add( Building(0,list.get(i),area.area+it+"层",it))
                                areaList.add(area.area+it+"层")
                            }
                            MyApplication.buildDBHelper.deleteBuildingInfoByParent(list.get(i))
                            MyApplication.buildDBHelper.insertBuildingInfoList(newlist)
                            withContext(Dispatchers.Main){
                                setFloorSpin(areaList)
                            }
                        }
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }
    fun setFloorSpin(list:MutableList<String>){
        _binding.FloorSpin.adapter = AreaListAdapter(requireContext(),list)
        _binding.FloorSpin.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                var area = MyApplication.buildDBHelper.queryBuildingIdInfoByArea(list.get(i))
                GlobalScope.launch(Dispatchers.IO) {
                    var respon = HttpTools.DianfeiGet(
                        "Room",
                        MyApplication.JFPTcookie,
                        "&buildid="+ buildId +"&floorid="+ area.areaId
                    )
                    Log.d("responbuild", "&buildid="+ buildId +"&floorid="+ area.areaId)
                    if(respon.contains("roomList")){

                        Log.d("responbuild", respon)
                        var jsonObject = JSONObject.parseObject(respon)

                        var areas = HttpTools.convertStringToMap(jsonObject.getString("roomList"))
                        Log.d("responbuild", area.areaId)

                        var newlist:MutableList<Building> = mutableListOf()
                        val areaList = mutableListOf<String>()
                        areas.forEach{
                            newlist.add( Building(0,list.get(i),it.value,it.key))
                            areaList.add(it.value)
                        }
                        MyApplication.buildDBHelper.deleteBuildingInfoByParent(list.get(i))
                        MyApplication.buildDBHelper.insertBuildingInfoList(newlist)
                        withContext(Dispatchers.Main){
                            setRoomSpin(areaList)
                        }
                    }

                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }
    }
    fun setRoomSpin(list:MutableList<String>){
        _binding.RoomSpin.adapter = AreaListAdapter(requireContext(),list)
        _binding.RoomSpin.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                var area = MyApplication.buildDBHelper.queryBuildingIdInfoByArea(list.get(i))
                GlobalScope.launch(Dispatchers.IO) {
                    var respon = HttpTools.DianfeiGet(
                        "RoomElec",
                        MyApplication.JFPTcookie,
                        "&roomid=" + area.areaId
                    )
                    if(respon.contains("meterId")){
                        var jsonObject = JSONObject.parseObject(respon)
                        var meterId = jsonObject.getString("meterId")
                        var respondi = HttpTools.DianfeiGet(
                            "Reserve",
                            MyApplication.JFPTcookie,
                            "&meterId=" + meterId
                        )
                        if(respondi.contains("remainPower")){
                            var jsonObject = JSONObject.parseObject(respondi)
                            var meterId = jsonObject.getString("remainPower")
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(),meterId,Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }
    }
    var xiaoliPath:File? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myBroadcastReceiver = MyBroadcastReceiver()
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        _binding.xiaoliIV.setOnClickListener {
            showPhotoPop(it.resources)
        }
        var areaList = MyApplication.buildDBHelper.queryBuildingsIdInfoByAreaParent("area")
        if(areaList.isNotEmpty()){
            var areas:MutableList<String> = mutableListOf()
            areaList.forEach {
                areas.add(it.area)
            }
            setAreaSpin(areas)
        }
        GlobalScope.launch(Dispatchers.IO) {
            // 在IO线程执行网络请求
            val result = HttpTools.YiyanGet()
            val json = JSONObject.parseObject(result)
            val yiyan:String = json.getString("hitokoto")
            val yiyanSign:String = "--"+json.getString("from")+" "+json.getString("from_who")
            Log.d("respon",result)

            var fileList:List<File> = HttpTools.getFilesInDirectory(requireContext().externalCacheDir?.path)
            var isXiaoLiHave =true
            val width = getResources().getDisplayMetrics().widthPixels
            fileList.forEach {
                if(it.name.contains("校历")){
                    isXiaoLiHave = false
                    xiaoliPath = it
                    withContext(Dispatchers.Main){
                        Glide.with(requireContext())
                            .load(it)
                            .override(width)
                            .placeholder(R.drawable.baseline_downloading_24)//图片加载出来前，显示的图片
                            .error(R.drawable.baseline_error_outline_24)//图片加载失败后，显示的图片
                            .into(_binding.xiaoliIV);
                    }
                }
            }
            if(isXiaoLiHave){
                HttpTools.getXiaoLi(requireContext())

            }
            withContext(Dispatchers.Main) {
                // 在主线程更新UI

                _binding.yiyanTv.setText(yiyan)
                _binding.yiyanSignTv.setText(yiyanSign)

            }
        }
        _binding.requestButton.setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString("platform","缴费平台")
            bundle.putString("action","request")
            intent.putExtra("bundle",bundle)
            requireContext().startActivity(intent)
        }
        return _binding.root
    }

    private fun showPhotoPop(resources: Resources) {
        // 初始化PopupWindow
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_photo_see, null);
        val btnclose: ImageButton = popupView.findViewById(R.id.photoClose);
        val photo:PhotoView = popupView.findViewById(R.id.popPhotoView);
        val popupWindow = PopupWindow(popupView,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT,true)
        photo.setOnClickListener {
            popupWindow.dismiss()
        }
        btnclose.setOnClickListener {
            popupWindow.dismiss()
        }
// 显示PopupWindow
        val width = getResources().getDisplayMetrics().widthPixels
        Glide.with(requireContext())
            .load(xiaoliPath)
            .override(width)
            .fitCenter()
            .placeholder(R.drawable.baseline_downloading_24)//图片加载出来前，显示的图片
            .error(R.drawable.baseline_error_outline_24)//图片加载失败后，显示的图片
            .into(photo);
        val rootview = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, null)
        dimBackground(0.5f); // 0.5f 表示半透明
        popupWindow.showAtLocation(rootview,Gravity.CENTER,0,0); // anchorView是触发PopupWindow显示的视图
        popupWindow.setOnDismissListener {
            dimBackground(1.0f)
        }

    }

    fun dimBackground(dimAmount: Float){
        var lp:WindowManager.LayoutParams = requireActivity().window.attributes;
        lp.alpha = dimAmount
        requireActivity().window.attributes = lp
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        val filter = IntentFilter("com.huanyu.RequestCookie")
        requireContext().registerReceiver(myBroadcastReceiver, filter)
        super.onStart()
    }

    override fun onStop() {
        requireContext().unregisterReceiver(myBroadcastReceiver);
        super.onStop()
    }
    override fun onResume() {
        GlobalScope.launch(Dispatchers.IO){
            var respon = HttpTools.DianfeiGet("Area",MyApplication.JFPTcookie,"")
            if(respon.contains("areaList")) {
                Log.d("responbuild", respon)
                var jsonObject = JSONObject.parseObject(respon)

                var areas = HttpTools.convertStringToMap(jsonObject.getString("areaList"))
                var newlist:MutableList<Building> = mutableListOf()
                val areaList = mutableListOf<String>()
                areas.forEach{
                    newlist.add( Building(0,"area",it.value,it.key))
                    areaList.add(it.value)
                }
                MyApplication.buildDBHelper.deleteBuildingInfoByParent("area")
                MyApplication.buildDBHelper.insertBuildingInfoList(newlist)
                withContext(Dispatchers.Main) {
                    setAreaSpin(areaList)
                }
            }

        }
        super.onResume()
    }
}