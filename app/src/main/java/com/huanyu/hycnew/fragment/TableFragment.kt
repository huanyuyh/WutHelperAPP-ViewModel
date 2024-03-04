package com.huanyu.hycnew.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.huanyu.hycnew.R
import com.huanyu.hycnew.activity.WebActivity
import com.huanyu.hycnew.adapter.MyFragmentPageAdapter
import com.huanyu.hycnew.adapter.WeekAdapter
import com.huanyu.hycnew.databinding.FragmentTableBinding
import com.huanyu.hycnew.utils.DateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


class TableFragment() : Fragment() {

    companion object {
        fun newInstance() = TableFragment()
    }
    lateinit var _binding:FragmentTableBinding
    private lateinit var viewModel: TableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTableBinding.inflate(layoutInflater, container, false)
        var localDate = LocalDate.now()
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        _binding.datePickerTv?.setText(localDate.format(formatter))
        _binding.datePickerTv?.setOnClickListener {
            var calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // 这里获取到选择的日期
                    val selectedDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString().padStart(2,'0')
                    _binding.datePickerTv?.setText(selectedDate)
                    var dateMap = DateUtils.calculateDaysAndWeeksFromDay("2023-09-04",selectedDate)
                    Log.d("weeksyes",dateMap.get("weeks").toString())
                    dateMap.get("weeks")?.let {
                        _binding.weekSpin.setSelection(it)
                        _binding.coursePager.setCurrentItem(it,false)}
                        // 你可以在这里处理日期
                }, year, month, day
            )
            datePickerDialog.show() // 显示对话框

        }
        var weekList = ArrayList<String>()
        for (i in 1..18){
            weekList.add("第${i}周")
        }
        Log.d("weekList",weekList.toString())
        _binding.weekSpin.adapter = WeekAdapter(requireContext(), weekList)
        Log.d("weekSpin",_binding.weekSpin.selectedItem.toString())

        _binding.weekSpin.onItemSelectedListener =object : OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?,i: Int, l: Long) {
                _binding.coursePager.currentItem = i
                _binding.weeksTv?.setText(_binding.weekSpin.selectedItem.toString())
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                adapterView?.setSelection(0)
            }
        }
        Log.d("weekSpin",_binding.weekSpin.selectedItem.toString())
        _binding.courseMoreBtn.setOnClickListener {
            showMoreMenu(it)
        }
        var fragmentlist = mutableListOf<Fragment>()
        for (i in 1..18){
            var fragment = TablePageFragment()
            fragment.week = i
            fragmentlist.add(fragment)

        }

        Log.d("fragmentlist",fragmentlist.toString())
        _binding.coursePager?.adapter = MyFragmentPageAdapter(fragmentlist,requireActivity())
        _binding.coursePager.registerOnPageChangeCallback(object :OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                _binding.weekSpin.setSelection(position)
                super.onPageSelected(position)
            }
        })
        _binding.coursePager.isUserInputEnabled = true
        _binding.coursePager.offscreenPageLimit = 3
        dateSelect("2023-09-04")
        return _binding.root
    }
    fun dateSelect(date:String){
        var dateMap = DateUtils.calculateDaysAndWeeksFromToday(date)
        Log.d("weeks",dateMap.get("weeks").toString())
        dateMap.get("weeks")?.let { _binding.weekSpin.setSelection(it)
        _binding.coursePager.setCurrentItem(it,false)}

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TableViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
//        _binding.coursePager.removeAllViews()
//        var fragmentlist = mutableListOf<Fragment>()
//        for (i in 1..18){
//            var fragment = TablePageFragment()
//            fragment.orientation = requireContext().resources.configuration.orientation
//            fragment.week = i
//            fragment.density = requireContext().resources.displayMetrics.density
//            fragmentlist.add(fragment)
//
//        }
//        _binding.coursePager?.adapter = MyFragmentPageAdapter(fragmentlist,requireActivity())
        super.onConfigurationChanged(newConfig)
    }
    @SuppressLint("CutPasteId")
    fun showMoreMenu(view: View){
        // 初始化PopupWindow
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_course_more, null);
        val btnImport:TextView = popupView.findViewById(R.id.importTv);
        val btnList:TextView = popupView.findViewById(R.id.listTv);
        val btnSettings:TextView = popupView.findViewById(R.id.settingsTv);
        val popupWindow = PopupWindow(popupView,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,true)

// 获取按钮并设置点击事件

        btnImport.setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString("platform","教务系统（智慧理工大）")
            bundle.putString("action","import")
            intent.putExtra("bundle",bundle)
            requireContext().startActivity(intent)
        }; // 替换为实际的方法


        btnList.setOnClickListener{  }; // 替换为实际的方法


        btnSettings.setOnClickListener{  }; // 替换为实际的方法
// 显示PopupWindow
        popupWindow.showAsDropDown(view); // anchorView是触发PopupWindow显示的视图

    }

}