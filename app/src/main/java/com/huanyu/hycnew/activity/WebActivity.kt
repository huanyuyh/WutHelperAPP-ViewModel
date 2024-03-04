package com.huanyu.hycnew.activity


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.R
import com.huanyu.hycnew.databinding.ActivityWebBinding
import com.huanyu.hycnew.fragment.HomeFragment


class WebActivity : AppCompatActivity() {
    lateinit var webView: WebView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.getBundleExtra("bundle")
        //视图绑定
        val _binding = ActivityWebBinding.inflate(layoutInflater)
        //viewModel实例化
        val viewModel = ViewModelProvider(this).get(WebActivityViewModel ::class.java)
        viewModel.context = this
        viewModel.activity = this
        viewModel.path = this.externalCacheDir!!.path+"/jwc.html"

//防止键盘影响布局        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        viewModel.action = bundle?.getString("action").toString()
//        if(viewModel.action.contains("import")){
//            showPopup()
//        }

        bundle?.let {
            var platform = MyApplication.userDBHelper.queryPlatformInfoByplatName(it.getString("platform"))
            Log.d("data",platform.toString())
            viewModel.tempUrl = platform.platUrl
            viewModel.webUrl = platform.platUrl
            _binding.editUrl.setText(platform.platName)
            Toast.makeText(this,it.getString("platform"),Toast.LENGTH_SHORT).show()
        }


        webView = _binding.webview
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        viewModel.initWebView(webView)
//        webView.loadUrl(viewModel.webUrl)
        viewModel.webTitle.observe(this, Observer {
            it?.let {
                _binding.editUrl.setText(it)
            }
        })
        viewModel.progressWeb.observe(this, Observer {
            it?.let {
                _binding.progressWeb.progress = it
                if(it>=100){
                    _binding.progressWeb.visibility =View.GONE
                }else{
                    _binding.progressWeb.visibility = View.VISIBLE
                }
            }
        })
        _binding.editUrl.setOnFocusChangeListener{view,hasFocus->
            if(hasFocus) {
                viewModel.urlFocus = true
                _binding.editUrl.setText(webView.url)
            }
            else {
                viewModel.urlFocus = false
                _binding.editUrl.setText(viewModel.webTitle.value)
            }
        }
        _binding.editUrl.addTextChangedListener {
            if(viewModel.urlFocus)
            viewModel.tempUrl = it.toString()
        }
        _binding.goUrlButton.setOnClickListener {
            _binding.editUrl.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(_binding.editUrl.windowToken, 0)
            viewModel.urlFocus = false
            viewModel.webLoadUrl()
        }
        _binding.backButton.setOnClickListener {
            viewModel.webBack()
        }
        _binding.forwardButton.setOnClickListener {
            viewModel.webForWard()
        }
        _binding.homeButton.setOnClickListener {
            viewModel.webHome()
        }
        _binding.refreshButton.setOnClickListener {
            viewModel.webRefresh()
        }
        _binding.backPageButton.setOnClickListener {
            this.finish()
        }
        _binding.siteMoreButton.setOnClickListener{
            showMyPopWindows(it)

        }
    }
    fun showMyPopWindows(view:View){
        val myPopWindows = LayoutInflater.from(this).inflate(R.layout.pop_web,null)
//        Toast.makeText(this,"yes",Toast.LENGTH_SHORT).show()
//        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val popupWindow = inflater.inflate(R.layout.web_pop,null)
        val textView:TextView = myPopWindows.findViewById(R.id.openBoswer)
        textView.setText("在浏览器打开")
        val popWindow = PopupWindow(myPopWindows,ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT,true)
//        popWindow.contentView = myPopWindows
//        popWindow.width = LayoutParams.WRAP_CONTENT
//        popWindow.height = LayoutParams.WRAP_CONTENT
        popWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//        val rootview = LayoutInflater.from(this).inflate(R.layout.activity_web, null)
        popWindow.showAsDropDown(view)
        backgroundAlpha(0.5f);
        textView.setOnClickListener {
            popWindow.dismiss()
            backgroundAlpha(1f);
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webView.url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun backgroundAlpha(alpha: Float) {
        val layoutParams = window.attributes
        layoutParams.alpha = alpha
        window.attributes = layoutParams
    }

    override fun onStart() {

        super.onStart()
    }

    override fun onStop() {

        super.onStop()
    }
}