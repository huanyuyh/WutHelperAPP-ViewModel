package com.huanyu.hycnew

import android.app.Application
import com.huanyu.hycnew.database.BuildDBHelper
import com.huanyu.hycnew.database.CourseDBHelper
import com.huanyu.hycnew.database.UserDBHelper
import com.huanyu.hycnew.entity.Platform
import com.huanyu.hycnew.utils.SharedPreferenceUtil
import main.java.bean.TimeDetail

class MyApplication:Application() {
    companion object{
        lateinit var userDBHelper:UserDBHelper
        lateinit var courseDBHelper:CourseDBHelper
        lateinit var buildDBHelper:BuildDBHelper
        lateinit var JFPTcookie:String

        fun newInstance()=Application()
    }
    init {
        userDBHelper = UserDBHelper.getInstance(this)
        courseDBHelper = CourseDBHelper.getInstance(this)
        buildDBHelper = BuildDBHelper.getInstance(this)
    }
    override fun onCreate() {
        super.onCreate()
        JFPTcookie = SharedPreferenceUtil(this).getString("JFPTCookie","null")
//        var userDBHelper:UserDBHelper = UserDBHelper.getInstance(this)
        userDBHelper.openWriteLink()
        userDBHelper.openReadLink()
        courseDBHelper.openReadLink()
        courseDBHelper.openWriteLink()
        buildDBHelper.openReadLink()
        buildDBHelper.openWriteLink()
        var courseTime:List<TimeDetail> = courseDBHelper.queryAllCourseTimeInfo()
        var platList:List<Platform> = userDBHelper.queryAllPlatformInfo()
        if(courseTime.isEmpty()==true) {
            val courseTimes: List<TimeDetail> = mutableListOf(
                TimeDetail(1, "08:00", "08:45"),
                TimeDetail(2, "08:50", "09:35"),
                TimeDetail(3, "09:55", "10:40"),
                TimeDetail(4, "10:45", "11:30"),
                TimeDetail(5, "11:35", "12:20"),
                TimeDetail(6, "14:00", "14:45"),
                TimeDetail(7, "14:50", "15:35"),
                TimeDetail(8, "15:40", "16:25"),
                TimeDetail(9, "16:45", "17:30"),
                TimeDetail(10, "17:35", "16:20"),
                TimeDetail(11, "19:00", "19:45"),
                TimeDetail(12, "19:50", "20:35"),
                TimeDetail(13, "20:40", "21:25"),
            )
            courseDBHelper.insertCourseTimeInfoList(courseTimes)
        }
        if(platList.isEmpty()==true){
            platList = mutableListOf(
                Platform(0,
                    "智慧理工大",
                    "http://zhlgd.whut.edu.cn/",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click();\n"),
                Platform(0,
                    "教务系统（智慧理工大）",
                    "http://sso.jwc.whut.edu.cn/Certification/index2.jsp",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"),
                Platform(0,
                    "教务系统",
                    "http://sso.jwc.whut.edu.cn/Certification/toIndex.do",
                    "教务系统",
                    "document.getElementById('username').value ='';\n" +
                            "document.getElementById('password').value ='';\n" +
                            "setTimeout(function() {\n" +
                            "document.getElementById('submit_id').click()\n" +
                            "}, 1000);\n"),
                Platform(0,
                    "教务系统(新)",
                    "http://jwxt.whut.edu.cn",
                    "智慧理工大",
                    "document.getElementById('tyrzBtn').click();\n"+
                            "setTimeout(function() {\n" +
                            "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"+
                            "}, 1000);\n"
                ),
                Platform(0,
                    "缴费平台",
                    "http://cwsf.whut.edu.cn",
                    "智慧理工大",
                    "document.getElementById('usercode').value ='';\n" +
                            "document.getElementById('passwd').value ='';\n" +
                            "formSubmit();\n"),
                Platform(0,
                    "智慧学工",
                    "https://talent.whut.edu.cn/",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"),
                Platform(0,
                    "校园地图（智慧理工大版）",
                    "http://gis.whut.edu.cn/index.shtml",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"),
                Platform(0,
                    "校园地图（微校园版）",
                    "http://gis.whut.edu.cn/mobile/index.html#/",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"),
                Platform(0,
                    "WebVPN",
                    "https://webvpn.whut.edu.cn/",
                    "校园网认证(WLAN)",
                    "document.getElementsByName('username')[0].value ='';\n" +
                            "document.getElementsByName('password')[0].value ='';\n" +
                            "document.getElementsByName('remember_cookie')[0].click()\n" +
                            "document.getElementById('login').click()\n"),
                Platform(0,
                    "学校邮箱",
                    "https://qy.163.com/login/",
                    "学校邮箱",
                    "document.getElementById('accname').value ='';\n" +
                            "document.getElementById('accpwd').value ='';\n" +
                            "document.getElementById('accautologin').checked\n" +
                            "document.getElementsByClassName('u-logincheck logincheck js-logincheck js-loginPrivate loginPrivate')[0].click()\n" +
                            "document.getElementsByClassName('w-button w-button-account js-loginbtn')[0].click()\n"),
                Platform(0,
                    "成绩查询",
                    "http://zhlgd.whut.edu.cn/tp_up/view?m=up#act=up/sysintegration/queryGrade",
                    "智慧理工大",
                    "document.getElementById('un').value ='';\n" +
                            "document.getElementById('pd').value ='';\n" +
                            "document.getElementById('index_login_btn').click()\n"),
                Platform(0,
                    "校园网认证(1.1.1.1)",
                    "http://1.1.1.1",
                    "校园网认证(WLAN)",
                    "document.getElementById('username').value ='';\n" +
                            "document.getElementById('password').value ='';\n" +
                            "checkForm()\n"),
                Platform(0,
                    "校园网认证(WLAN)",
                    "http://172.30.21.100",
                    "校园网认证(WLAN)",
                    "document.getElementById('username').value ='';\n" +
                            "document.getElementById('password').value ='';\n" +
                            "checkForm()\n"),
                Platform(0,
                    "校园网认证(isp)",
                    "http://172.30.21.100/",
                    "校园网认证(WLAN)",
                    "document.getElementById('username').value ='';\n" +
                            "document.getElementById('password').value ='';\n" +
                            "checkForm()\n"),
                Platform(0,
                    "校园主页",
                    "http://i.whut.edu.cn/",
                    "null",
                    "null"),
            )
            userDBHelper.insertPlatformInfos(platList)
            userDBHelper.closeLink()
        }
    }
}