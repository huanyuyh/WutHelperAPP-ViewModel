package com.huanyu.hycnew.fragment

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.R
import com.huanyu.hycnew.databinding.FragmentTableBinding
import com.huanyu.hycnew.databinding.FragmentTablePageBinding
import com.huanyu.hycnew.utils.DateUtils

class TablePageFragment() : Fragment() {
    private lateinit var viewModel: TablePageViewModel
    var week:Int = 0;
    lateinit var _binding:FragmentTablePageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTablePageBinding.inflate(layoutInflater, container, false)
        showDate(gridLayout = _binding.girdLayout)
        showtime(gridLayout = _binding.girdLayout)
        showCourse(gridLayout = _binding.girdLayout)
        return _binding.root
    }
    fun showDate(gridLayout: GridLayout){
        val orientation = getResources().configuration.orientation
        val density = getResources().displayMetrics.density
        val height = getResources().getDisplayMetrics().heightPixels
        val width = getResources().getDisplayMetrics().widthPixels
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_date,null)
        val textView = view.findViewById<TextView>(R.id.dateTv)
        val weekTv = view.findViewById<TextView>(R.id.weekdayTv)
        val cardView = view.findViewById<CardView>(R.id.dateCard)
        weekTv.setText(week.toString())
        textView.setTextSize(12f)
        textView.setText("2023")
        cardView.setBackgroundColor(Color.WHITE)
        val params = GridLayout.LayoutParams()
        Log.d("width",width.toString())
        if(orientation==Configuration.ORIENTATION_LANDSCAPE){
            params.width=((width*0.92*0.92)/15).toInt()
            params.height=ConstraintLayout.LayoutParams.WRAP_CONTENT
        }else{
            params.width=((width)/23*2).toInt()
            params.height=ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        params.rowSpec = GridLayout.spec(0, 1) // 位于第0行，跨越1行
        params.columnSpec = GridLayout.spec(0, 1) // 位于第0列，跨越1列
        view.setLayoutParams(params);
        gridLayout.addView(view);
        val weekList = mutableListOf<String>("周一","周二","周三","周四","周五","周六","周日",)
        for(i in 1..7){
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_date,null)
            val textView = view.findViewById<TextView>(R.id.dateTv)
            val weekTv = view.findViewById<TextView>(R.id.weekdayTv)
            val cardView = view.findViewById<CardView>(R.id.dateCard)
            val newDate = DateUtils.addDaysToDateReturnMonthDay("2023-09-03",((week-1)*7+i).toLong())
            textView.setText(newDate)
            weekTv.setText(weekList.get(i-1))
            cardView.setBackgroundColor(Color.WHITE)
            val params = GridLayout.LayoutParams()
            Log.d("width",width.toString())
            if(orientation==Configuration.ORIENTATION_LANDSCAPE){
                params.width=((width*0.92*0.92)/15*2).toInt()
                params.height=ConstraintLayout.LayoutParams.WRAP_CONTENT
            }else{
                params.width=((width)/23*3).toInt()
                params.height=ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
            params.rowSpec = GridLayout.spec(0, 1) // 位于第0行，跨越1行
            params.columnSpec = GridLayout.spec(i, 1) // 位于第0列，跨越1列
            view.setLayoutParams(params);
            gridLayout.addView(view);
        }
        cardView.setBackgroundColor(Color.WHITE)
    }
    fun showtime(gridLayout: GridLayout){
        val orientation = getResources().configuration.orientation
        val density = getResources().displayMetrics.density
        val height = getResources().getDisplayMetrics().heightPixels
        val width = getResources().getDisplayMetrics().widthPixels
        val timeList = MyApplication.courseDBHelper.queryAllCourseTimeInfo()
        for ((index, timeDetail) in timeList.withIndex()) {

            val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_course_time,null)
            val nodeTv = view.findViewById<TextView>(R.id.nodeTv)
            val startTv = view.findViewById<TextView>(R.id.startTimeTv)
            val endTv = view.findViewById<TextView>(R.id.endTimeTv)
            nodeTv.setText(timeDetail.node.toString())
            startTv.setText(timeDetail.startTime)
            endTv.setText(timeDetail.endTime)
            val cardView = view.findViewById<CardView>(R.id.courseTimeCard)
            cardView.setBackgroundColor(Color.WHITE)
            val params = GridLayout.LayoutParams()
            Log.d("width",width.toString())
            if(orientation==Configuration.ORIENTATION_LANDSCAPE){
                params.width=((width*0.92*0.92)/15).toInt()
                params.height=80*density.toInt()
                startTv.setTextSize(10F)
                endTv.setTextSize(10F)
            }else{
                params.width=((width)/23*2).toInt()
                params.height=height/10
                startTv.setTextSize(10F)
                endTv.setTextSize(10F)
            }

            params.rowSpec = GridLayout.spec(index+1, 1) // 位于第0行，跨越1行
            params.columnSpec = GridLayout.spec(0, 1) // 位于第0列，跨越1列
            view.setLayoutParams(params);
            gridLayout.addView(view);
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)
    }
    fun showCourse(gridLayout: GridLayout){
        val orientation = getResources().configuration.orientation
        val density = getResources().displayMetrics.density
        val courseList = MyApplication.courseDBHelper.queryAllCourseInfo()
        val height = getResources().getDisplayMetrics().heightPixels
        val width = getResources().getDisplayMetrics().widthPixels
        courseList.forEach{
            if(it.startWeek<=week&&it.endWeek>=week){
                val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_course,null)
                val nameTv = view.findViewById<TextView>(R.id.courseNameTv)
                val teacherTv = view.findViewById<TextView>(R.id.courseTeacher)
                val positionTv = view.findViewById<TextView>(R.id.coursePosition)
                val timeTv = view.findViewById<TextView>(R.id.courseTime)
                val cardView = view.findViewById<CardView>(R.id.courseCard)

                nameTv.setText(it.name)
                teacherTv.setText(it.teacher)
                positionTv.setText(it.room)
                timeTv.setText(it.startNode.toString()+"-"+it.endNode.toString())
                cardView.setBackgroundColor(Color.WHITE)
                val params = GridLayout.LayoutParams()
                Log.d("width",width.toString())
                if(orientation==Configuration.ORIENTATION_LANDSCAPE){
                    params.width=((width*0.92*0.92)/15*2).toInt()
                    params.height=80*density.toInt()*(it.endNode-it.startNode+1)
                }else{
                    params.width=((width)/23*3).toInt()
                    params.height=height/10*(it.endNode-it.startNode+1)
                }
                params.rowSpec = GridLayout.spec(it.startNode, it.endNode-it.startNode+1) // 位于第0行，跨越1行
                params.columnSpec = GridLayout.spec(it.day, 1) // 位于第0列，跨越1列
                view.setLayoutParams(params);
                gridLayout.addView(view);
            }

        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TablePageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}