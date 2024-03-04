package com.huanyu.hycnew.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.R
import com.huanyu.hycnew.adapter.PlatListAdapter
import com.huanyu.hycnew.adapter.UserListAdapter
import com.huanyu.hycnew.databinding.ActivityUserBinding
import com.huanyu.hycnew.entity.User


class UserActivity : AppCompatActivity() {
    val userDBHelper = MyApplication.userDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        val _binding = ActivityUserBinding.inflate(layoutInflater)
        //viewModel实例化
        val viewModel = ViewModelProvider(this).get(WebActivityViewModel ::class.java)
        //防止键盘影响布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        userDBHelper.openReadLink()
        userDBHelper.openWriteLink()
        var userList:List<User> = userDBHelper.queryAllUserInfo()
        Log.d("data",userList.toString())
        val userAdapter = UserListAdapter(this,userList)
        _binding.recyclerView.adapter = userAdapter
        val layoutManager = LinearLayoutManager(this)
        _binding.recyclerView.setLayoutManager(layoutManager)
        _binding.userSaveButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding.recyclerView.windowToken, 0)
            userDBHelper.deleteAllUserInfo()
            userDBHelper.insertUserInfoListSaveId(userAdapter.mUserList)
        }
        _binding.newUserButton.setOnClickListener {
            showNewUserPopWindows()
        }
        _binding.userbackButton.setOnClickListener {
            finish()
        }
    }
    fun showNewUserPopWindows(){
        val user = User(0,"智慧理工大","","")
        val myPopWindows = LayoutInflater.from(this).inflate(R.layout.pop_new_user,null)
        val spinner = myPopWindows.findViewById<Spinner>(R.id.platformspinner)
        val userEdit = myPopWindows.findViewById<EditText>(R.id.usereditText)
        val passEdit = myPopWindows.findViewById<EditText>(R.id.passeditText)
        val insertBtn = myPopWindows.findViewById<Button>(R.id.insertbutton)
        val clearBtn = myPopWindows.findViewById<Button>(R.id.clearbutton)
        val cancelBtn = myPopWindows.findViewById<Button>(R.id.cancelbutton)
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val density = displayMetrics.density
        val popWindow = PopupWindow(myPopWindows,
            width/5*4,
            LayoutParams.WRAP_CONTENT,true)
        popWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val rootview = LayoutInflater.from(this).inflate(R.layout.activity_user, null)
        popWindow.showAtLocation(rootview,Gravity.CENTER,0,0)
        cancelBtn.setOnClickListener {
            popWindow.dismiss()
        }
        clearBtn.setOnClickListener {
            userEdit.setText("")
            passEdit.setText("")
        }
        userEdit.addTextChangedListener {
            user.name = it.toString()
        }
        passEdit.addTextChangedListener {
            user.pass = it.toString()
        }
        val platList:List<String> = mutableListOf(
            "教务系统",
            "智慧理工大",
            "校园网认证(WLAN)",
            "学校邮箱",
        )
        insertBtn.setOnClickListener {
            recreate()
            userDBHelper.insertUserInfo(user)
        }
        spinner.adapter = PlatListAdapter(this,platList)
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                user.platform = platList.get(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



    }
}