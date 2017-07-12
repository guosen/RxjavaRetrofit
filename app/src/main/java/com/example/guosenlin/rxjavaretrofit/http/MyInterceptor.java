package com.example.guosenlin.rxjavaretrofit.http;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by guosenlin on 2017/7/12.
 */

public class MyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (request.method().equals("GET")) {
            request = addGetParams(request);
        } else if (request.method().equals("POST")) {
            //request = addPostParams(request);
        }

        return chain.proceed(request);
    }

    private static Request addGetParams(Request request) {
        //添加公共参数
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .addQueryParameter("version","" )
                .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();

        //添加签名
        Set<String> nameSet = httpUrl.queryParameterNames();
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        Collections.sort(nameList);


        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < nameList.size(); i++) {
            buffer.append("&").append(nameList.get(i)).append("=").append(httpUrl.queryParameterValues(nameList.get(i)) != null &&
                    httpUrl.queryParameterValues(nameList.get(i)).size() > 0 ? httpUrl.queryParameterValues(nameList.get(i)).get(0) : "");
        }

        httpUrl = httpUrl.newBuilder()
                .addQueryParameter("sign", EncryptDecryptTool.stringToMd5(buffer.toString()))
                .build();
        request = request.newBuilder().url(httpUrl).build();
        Log.i("url",httpUrl.toString());
        return request;


    }
}
