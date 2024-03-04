package com.huanyu.hycnew.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpTools {

    public static String YiyanGet(){
        String respon = "fault";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://v1.hitokoto.cn/?encode=json")
                .method("POST", body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            respon = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return respon;
    }
    public static String DianfeiGet(String action,String cookie,String data) {
        String url = "http://cwsf.whut.edu.cn/";
        switch (action){
            case "Area":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/getAreaInfo";
                break;
            case "Build":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/queryBuildList";
                break;
            case "Floor":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/queryFloorList";
                break;
            case "Room":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/getRoomInfo";
                break;
            case "RoomElec":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/queryRoomElec";
                break;
            case "Reserve":
                url = "http://cwsf.whut.edu.cn/MNetWorkUI/queryReserve";
                break;
        }
        String respon = "fault";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "factorycode=E035"+data);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-GB;q=0.8,en-US;q=0.7,en;q=0.6")
                .addHeader("Cookie", cookie)
                .addHeader("Host", "cwsf.whut.edu.cn")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.76")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Proxy-Connection", "keep-alive")
                .build();
        Response response = null;
        int maxRetries = 3; // 最大重试次数
        int retryCount = 0; // 当前重试次数
        int retryWaitTime = 5000; // 重试等待时间（毫秒）
        while (retryCount < maxRetries){
            try {
                response = client.newCall(request).execute();
                respon = response.body().string();
                return respon;
            } catch (IOException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new RuntimeException(e); // 如果重试次数用完，抛出异常
                }
                try {
                    Thread.sleep(retryWaitTime); // 等待一段时间后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 保持中断状态
                    throw new RuntimeException(ie);
                }
            }
        }

        return respon;
    }

    public static Map<String, String> convertStringToMap(String input) {
        Map<String, String> map = new HashMap<>();
        input = input.replace("\"","");
        input = input.replace("[","");
        input = input.replace("]","");
        String[] pairs = input.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split("@");
//            keyValue[0].replace("\"","");
//            keyValue[1].replace("\"","");
            if (keyValue.length == 2) { // 确保拆分后有两个部分
                map.put(keyValue[0], keyValue[1]);
            }
        }

        return map;
    }

    public static void getXiaoLi(Context context){
        String xiaoliName = null;
        String xiaoliUrl = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://i.whut.edu.cn/xl")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String respon = response.body().string();
            if(respon.contains("学期校历")){
               xiaoliName = respon.substring(respon.indexOf("title=\"")+"title=\"".length()
                        ,respon.indexOf("学期校历")+"学期校历".length());
                Log.d("xiaoli",xiaoliName);
                xiaoliUrl = "http://i.whut.edu.cn/xl"+xiaoliName.substring(xiaoliName.lastIndexOf("a href=\".")+"a href=\".".length(),xiaoliName.lastIndexOf("\" title="));
                Log.d("xiaoli",xiaoliUrl);
                xiaoliName = xiaoliName.substring(xiaoliName.lastIndexOf("title=\"")+"title=\"".length());
                Log.d("xiaoli",xiaoliName);

            }
        } catch (IOException e) {

        }
        Log.d("xiaoli",xiaoliUrl);
        if(xiaoliUrl.contains("http")){
            OkHttpClient clientpng = new OkHttpClient().newBuilder()
                    .build();
            Request requestpng = new Request.Builder()
                    .url(xiaoliUrl)
                    .build();
            try {
                Response responsepng = clientpng.newCall(requestpng).execute();
                String responpng = responsepng.body().string();
                if(responpng.contains("text-align: center;")){
                    String xiaoliPng = responpng.substring(responpng.indexOf("\"text-align: center;\"><a href=\".")+"\"text-align: center;\"><a href=\".".length());
                    xiaoliPng = xiaoliPng.substring(0,xiaoliPng.indexOf("\"") );
                    Log.d("xiaoli",xiaoliPng);
                    String xiaoliPngUrl = xiaoliUrl.substring(0,xiaoliUrl.lastIndexOf("/"))+xiaoliPng;
                    Log.d("xiaoli",xiaoliPngUrl);
                    downloadImage(xiaoliPngUrl,xiaoliName+".png",context);
                }
            } catch (IOException e) {

            }
        }

    }

    public static void downloadImage(String url, String fileName, Context context) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }
                saveImage(response.body().byteStream(), fileName,context);
            }
        });
    }

    public static void saveImage(InputStream imageStream, String fileName, Context context) {
        File picturesDirectory = context.getExternalCacheDir();
        File imageFile = new File(picturesDirectory, fileName);

        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<File> getFilesInDirectory(String directoryPath) {
        List<File> fileList = new ArrayList<>();
        File directory = new File(directoryPath);

        // 检查directory是否是一个目录
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file);
                    // 如果需要递归查找子目录中的文件，可以在这里递归调用
                }
            }
        }

        return fileList;
    }
}
