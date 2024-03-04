package com.huanyu.hycnew.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson2.JSONObject
import com.huanyu.hyc.Utils.FileUtil
import com.huanyu.hycnew.MyApplication
import com.huanyu.hycnew.course.WUTParser
import com.huanyu.hycnew.utils.HttpTools
import com.huanyu.hycnew.utils.SharedPreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WebActivityViewModel:ViewModel() {
    var webUrl:String = "zhlgd.whut.edu.cn"
    var tempUrl:String = "zhlgd.whut.edu.cn"
    lateinit var context:Context
    lateinit var activity: WebActivity
    lateinit var action:String
    lateinit var path:String
    var urlFocus:Boolean = false
    val webTitle:LiveData<String>
        get() = _webTitle
    private val _webTitle = MutableLiveData<String>("Hello")
    val progressWeb:LiveData<Int>
        get() = _progressWeb
    private val _progressWeb = MutableLiveData<Int>(-1)
    lateinit var mwebView: WebView
    fun webLoadUrl(){
        mwebView.loadUrl(tempUrl)
    }
    fun webBack(){
        mwebView.goBack()
    }
    fun webForWard(){
        mwebView.goForward()
    }
    fun webHome(){
        mwebView.loadUrl(webUrl)
    }
    fun webRefresh(){
        mwebView.clearCache(true)
        mwebView.reload()
    }
    private fun showPopup() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("导入课程表")
        builder.setMessage("确认导出课程表吗？")
        builder.setPositiveButton("确定",
            DialogInterface.OnClickListener { dialog, which ->
                // 在这里处理点击确定按钮的事件
                val html = FileUtil.readText(path)
                html?.let {
                    val wutParser = WUTParser(it)
                    val courseList = wutParser.generateCourseList()
                    Log.d("course",courseList.toString())
                    MyApplication.courseDBHelper.deleteAllCourseTimeInfo()
                    MyApplication.courseDBHelper.insertCourseInfoList(courseList)
                }

            })
        builder.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialog, which ->
                // 在这里处理点击取消按钮的事件
                activity.finish()
            })
        builder.create().show()
    }
    private fun sendMyBroadcast() {
        val intent = Intent("com.huanyu.RequestCookie")
        intent.putExtra("message", "yes")
        context.sendBroadcast(intent)
    }
    fun extractHTML() {
        mwebView.evaluateJavascript(
            "(function() { return document.documentElement.outerHTML; })();"
        ) { html ->
            // 在这里处理HTML内容
            // html是一个包含整个网页HTML内容的字符串
            Log.d("HTML", html)
            if(html.contains("欢迎")){

            }
        }
    }
    //view?.loadUrl("javascript:window.AndroidInterface.getSource('<head>'+" +
    //                            "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    private class JavaScriptInterface { // 你可以在这里定义其他与JavaScript交互的方法
        @JavascriptInterface
        fun getSource(html: String?) {
            Log.d("html=", html!!)

        }
    }
    @SuppressLint("JavascriptInterface")
    fun initWebView(webView: WebView){
        mwebView = webView
        webView.loadUrl(webUrl)
        webView.addJavascriptInterface(JavaScriptInterface(),"AndroidInterface")
        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                _webTitle.value = view?.title
                if (action.contains("request")){
                    // 获取Cookie
                    val cookieManager: CookieManager = CookieManager.getInstance()
                    val url = url
                    val cookies: String = cookieManager.getCookie(url)
                    MyApplication.JFPTcookie = cookies
                    if(cookies.contains("JSESSIONID")){
                        if(cookies.substring(cookies.indexOf("JSESSIONID")+"JSESSIONID".length+1)
                                .contains("JSESSIONID")){
                            GlobalScope.launch(Dispatchers.IO){
                                var respon = HttpTools.DianfeiGet("Area",MyApplication.JFPTcookie,"")
                                Log.d("responbuild",respon)
                                if(respon.contains("areaList")){
                                    sendMyBroadcast()
                                    SharedPreferenceUtil(context).putString("JFPTcookie",cookies)
                                    withContext(Dispatchers.Main) {

                                        activity.finish()
                                    }
                                }


                            }

                        }
                    }


                }
                if(action.contains("import")){
                    val downloadjs = "var context = '<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>';\n" +
                            "console.log(context);"
                    view?.evaluateJavascript(downloadjs, ValueCallback {
                    })
                }


                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                _progressWeb.value = -1
                view?.evaluateJavascript("var temp = alert;\n" +
                        "alert=null;\n" +
                        "alert(1);\n" +
                        "alert=temp; ", ValueCallback {

                } )
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if(null == request?.url) return false
                val showOverrideUrl = request?.url.toString()
                try {
                    if (!showOverrideUrl.startsWith("http://")
                        && !showOverrideUrl.startsWith("https://")) {
                        //处理非http和https开头的链接地址
                        Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            view?.context?.applicationContext?.startActivity(this)
                        }
                        return true
                    }else{
                        view?.loadUrl(showOverrideUrl)
                        return true
                    }
                }catch (e:Exception){
                    //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)

            }
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                //自行处理....
            }
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed();
                super.onReceivedSslError(view, handler, error)
            }
        }
        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                _progressWeb.value = newProgress
                super.onProgressChanged(view, newProgress)
            }
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    if(action.contains("import")&&consoleMessage.message().contains("欢迎")) {
                        showPopup()
                        Log.d("html=", consoleMessage.message())
                        FileUtil.saveText(path = path, text = consoleMessage.message()?:"")
                    }

                }
                return super.onConsoleMessage(consoleMessage)
            }
        }
        webView.settings.apply {
            //支持js交互
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls =false
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            defaultTextEncodingName = "utf-8"
            userAgentString = "Mozilla/5.0 (Linux; Android 7.0; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/10.3 SearchCraft/2.6.2 (Baidu; P1 7.0)"
            setDomStorageEnabled(true)
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }
}