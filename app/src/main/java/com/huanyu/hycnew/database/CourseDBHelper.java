package com.huanyu.hycnew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huanyu.connect.Entity.Course;
import com.huanyu.hycnew.course.CourseDetail;
import com.huanyu.hycnew.entity.Platform;
import com.huanyu.hycnew.entity.User;

import java.util.ArrayList;
import java.util.List;

import main.java.bean.TimeDetail;

public class CourseDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Courses.db";
    // 商品信息表
    private static final String TABLE_COURSE_INFO = "course_info";
    private static final String TABLE_COURSE_TIME_INFO = "course_time_info";
    // 购物车信息表
    private static final String TABLE_COURSE_LIST_INFO = "course_list_info";
    private static final int DB_VERSION = 1;
    private static CourseDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private CourseDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static CourseDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new CourseDBHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    // 创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建商品信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_TIME_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " node INTEGER NOT NULL," +
                " startTime VARCHAR NOT NULL," +
                " endTime VARCHAR NOT NULL);";
        db.execSQL(sql);

        // 创建购物车信息表
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_LIST_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name VARCHAR NOT NULL," +
                " room VARCHAR NOT NULL," +
                " teacher VARCHAR NOT NULL," +
                " time INTEGER NOT NULL," +
                " credit FLOAT NOT NULL," +
                " note VARCHAR NOT NULL);";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name VARCHAR NOT NULL," +
                " day INTEGER NOT NULL," +
                " room VARCHAR NOT NULL," +
                " teacher VARCHAR NOT NULL," +
                " startNode INTEGER NOT NULL," +
                " endNode INTEGER NOT NULL," +
                " startWeek INTEGER NOT NULL," +
                " endWeek INTEGER NOT NULL," +
                " type INTEGER NOT NULL," +
                " credit FLOAT NOT NULL," +
                " note VARCHAR NOT NULL," +
                " startTime VARCHAR NOT NULL," +
                " endTime VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 添加多条用户信息
    public void insertCourseTimeInfoList(List<TimeDetail> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (TimeDetail timeDetail : list) {
                ContentValues values = new ContentValues();
                values.put("node", timeDetail.getNode());
                values.put("startTime", timeDetail.getStartTime());
                values.put("endTime", timeDetail.getEndTime());
                mWDB.insert(TABLE_COURSE_TIME_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }

    public void insertCourseInfoList(List<Course> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (Course course : list) {
                ContentValues values = new ContentValues();
                values.put("name", course.getName());
                values.put("day", course.getDay());
                values.put("room", course.getRoom());
                values.put("teacher", course.getTeacher());
                values.put("startNode", course.getStartNode());
                values.put("endNode", course.getEndNode());
                values.put("startWeek", course.getStartWeek());
                values.put("endWeek", course.getEndWeek());
                values.put("type", course.getType());
                values.put("credit", course.getCredit());
                values.put("note", course.getNote());
                values.put("startTime", course.getStartTime());
                values.put("endTime", course.getEndTime());
                mWDB.insert(TABLE_COURSE_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }
    public void insertCourseListInfoList(List<CourseDetail> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (CourseDetail courseDetail: list) {
                ContentValues values = new ContentValues();
                values.put("name", courseDetail.getName());
                values.put("room", courseDetail.getRoom());
                values.put("teacher", courseDetail.getTeacher());
                values.put("time", courseDetail.getTime());
                values.put("credit", courseDetail.getCredit());
                values.put("note", courseDetail.getNote());
                mWDB.insert(TABLE_COURSE_LIST_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }

    // 查询所有的用户信息
    public List<Course> queryAllCourseInfo() {
        String sql = "select * from " + TABLE_COURSE_INFO;
        List<Course> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
//            int _id = cursor.getInt(0);
            String name = cursor.getString(1);
            int day = cursor.getInt(2);
            String room = cursor.getString(3);
            String teacher = cursor.getString(4);
            int startNode = cursor.getInt(5);
            int endNode = cursor.getInt(6);
            int startWeek = cursor.getInt(7);
            int endWeek = cursor.getInt(8);
            int type = cursor.getInt(9);
            float credit = cursor.getFloat(10);
            String note = cursor.getString(11);
            String startTime = cursor.getString(12);
            String endTime = cursor.getString(13);
            Course info = new Course(name,day,room,teacher,startNode,endNode,startWeek,endWeek,type,credit,note,startTime,endTime);
            list.add(info);
        }
        cursor.close();
        return list;
    }
    public List<CourseDetail> queryAllCourseDetailInfo() {
        String sql = "select * from " + TABLE_COURSE_LIST_INFO;
        List<CourseDetail> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String name = cursor.getString(1);
            String room = cursor.getString(2);
            String teacher = cursor.getString(3);
            String time = cursor.getString(4);
            float credit = cursor.getFloat(5);
            String note = cursor.getString(6);

            CourseDetail info = new CourseDetail(_id,name,room,teacher,time,credit,note);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public List<TimeDetail> queryAllCourseTimeInfo() {
        String sql = "select * from " + TABLE_COURSE_TIME_INFO;
        List<TimeDetail> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
//            int _id = cursor.getInt(0);
            int node = cursor.getInt(1);
            String startTime = cursor.getString(2);
            String endTime = cursor.getString(3);
            TimeDetail info = new TimeDetail(node,startTime,endTime);
            list.add(info);
        }
        cursor.close();
        return list;
    }



    // 删除所有购物车信息
    public void deleteAllCourseListInfo() {
        mWDB.delete(TABLE_COURSE_LIST_INFO, "1=1", null);
    }
    public void deleteAllCourseInfo() {
        mWDB.delete(TABLE_COURSE_INFO, "1=1", null);
    }
    public void deleteAllCourseTimeInfo() {
        mWDB.delete(TABLE_COURSE_TIME_INFO, "1=1", null);
    }
}
