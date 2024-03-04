package com.huanyu.hycnew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.huanyu.hycnew.entity.Platform;
import com.huanyu.hycnew.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Users.db";
    // 商品信息表
    private static final String TABLE_USER_INFO = "user_info";
    // 购物车信息表
    private static final String TABLE_PLATFORM_INFO = "platform_info";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static UserDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " platform VARCHAR NOT NULL," +
                " name VARCHAR NOT NULL," +
                " pass VARCHAR NOT NULL);";
        db.execSQL(sql);

        // 创建购物车信息表
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_PLATFORM_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " platName VARCHAR NOT NULL," +
                " platUrl VARCHAR NOT NULL," +
                " userPLat VARCHAR NOT NULL," +
                " webJs VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 添加多条用户信息
    public void insertUserInfoList(List<User> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (User user : list) {
                ContentValues values = new ContentValues();
                values.put("platform", user.getPlatform());
                values.put("name", user.getName());
                values.put("pass", user.getPass());
                mWDB.insert(TABLE_USER_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }
    // 添加多条用户信息保留id
    public void insertUserInfoListSaveId(List<User> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (User user : list) {
                ContentValues values = new ContentValues();
                values.put("_id", user.get_id());
                values.put("platform", user.getPlatform());
                values.put("name", user.getName());
                values.put("pass", user.getPass());
                mWDB.insert(TABLE_USER_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }
    // 添加1条用户信息
    public void insertUserInfo(User user) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
                ContentValues values = new ContentValues();
                values.put("platform", user.getPlatform());
                values.put("name", user.getName());
                values.put("pass", user.getPass());
                mWDB.insert(TABLE_USER_INFO, null, values);
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }

    // 查询所有的用户信息
    public List<User> queryAllUserInfo() {
        String sql = "select * from " + TABLE_USER_INFO;
        List<User> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String platform = cursor.getString(1);
            String user = cursor.getString(2);
            String pass = cursor.getString(3);
            User info = new User(_id,platform,user,pass);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    // 根据平台查询用户信息
    private User queryUserInfoByPlatform(String inPlatform) {
        Cursor cursor = mRDB.query(TABLE_USER_INFO, null, "platform=?", new String[]{String.valueOf(inPlatform)}, null, null, null);
        User info = null;
        if (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String platform = cursor.getString(1);
            String user = cursor.getString(2);
            String pass = cursor.getString(3);
            info = new User(_id,platform,user,pass);
        }
        return info;
    }
    // 根据ID查询用户信息
    private User queryUserInfoById(int id) {
        Cursor cursor = mRDB.query(TABLE_USER_INFO, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        User info = null;
        if (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String platform = cursor.getString(1);
            String user = cursor.getString(2);
            String pass = cursor.getString(3);
            info = new User(_id,platform,user,pass);
        }
        return info;
    }

    // 统计用户的总数量
    public int countUserInfo() {
        int count = 0;
        String sql = "select sum(count) from " + TABLE_USER_INFO;
        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    // 更新用户信息
    public void updateUserInfo(User inUser) {
        // 如果购物车中不存在该商品，添加一条信息
        User user = queryUserInfoByPlatform(inUser.getPlatform());
        ContentValues values = new ContentValues();
        values.put("platform", inUser.getPlatform());
        if (user == null) {
//            values.put("platform", user.getPlatform());
            values.put("name", inUser.getName());
            values.put("pass", inUser.getPass());
            mWDB.insert(TABLE_USER_INFO, null, values);
        } else {
            // 如果购物车中已经存在该商品，更新商品数量
            values.put("_id", user.get_id());
            values.put("name", inUser.getName());
            values.put("pass", inUser.getPass());
            mWDB.update(TABLE_USER_INFO, values, "platform=?", new String[]{String.valueOf(inUser.getPlatform())});
        }
    }
    public void updateUserInfoByID(User inUser) {
        // 如果购物车中不存在该商品，添加一条信息
        User user = queryUserInfoById(inUser.get_id());
        ContentValues values = new ContentValues();
        values.put("platform", inUser.getPlatform());
        if (user == null) {
//            values.put("platform", user.getPlatform());
            values.put("name", inUser.getName());
            values.put("pass", inUser.getPass());
            mWDB.insert(TABLE_USER_INFO, null, values);
        } else {
            // 如果购物车中已经存在该商品，更新商品数量
            values.put("_id", user.get_id());
            values.put("name", inUser.getName());
            values.put("pass", inUser.getPass());
            mWDB.update(TABLE_USER_INFO, values, "_id=?", new String[]{String.valueOf(inUser.get_id())});
        }
    }

    public void insertPlatformInfos(List<Platform> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (Platform platform : list) {
                ContentValues values = new ContentValues();
                values.put("platName", platform.getPlatName());
                values.put("platUrl", platform.getPlatUrl());
                values.put("userPlat", platform.getUserPLat());
                values.put("webJs", platform.getWebJs());
                mWDB.insert(TABLE_PLATFORM_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }

    // 查询购物车中所有的信息列表
    public List<Platform> queryAllPlatformInfo() {
        List<Platform> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_PLATFORM_INFO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String platName = cursor.getString(1);
            String platUrl = cursor.getString(2);
            String userPlat = cursor.getString(3);
            String webJs = cursor.getString(4);
            Platform info = new Platform(id,platName,platUrl,userPlat,webJs);
            list.add(info);
        }
        return list;
    }

    // 根据商品ID查询商品信息
    public Platform queryPlatformInfoByplatName(String mplatName) {
        Platform info = null;
        Cursor cursor = mRDB.query(TABLE_PLATFORM_INFO, null, "platName=?", new String[]{String.valueOf(mplatName)}, null, null, null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String platName = cursor.getString(1);
            String platUrl = cursor.getString(2);
            String userPlat = cursor.getString(3);
            String webJs = cursor.getString(4);
            info = new Platform(id,platName,platUrl,userPlat,webJs);
        }
        return info;
    }

    // 根据商品ID删除购物车信息
    public void deleteUserInfoByUserId(int userId) {
        mWDB.delete(TABLE_USER_INFO, "_id=?", new String[]{String.valueOf(userId)});
    }

    // 删除所有购物车信息
    public void deleteAllPlatformInfo() {
        mWDB.delete(TABLE_PLATFORM_INFO, "1=1", null);
    }
    public void deleteAllUserInfo() {
        mWDB.delete(TABLE_USER_INFO, "1=1", null);
    }
}
