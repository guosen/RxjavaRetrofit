package com.example.guosenlin.rxjavaretrofit.http;

import android.util.Log;

import com.google.gson.Gson;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Charles on 2016/3/17.
 */
class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
            Log.d("Network", "response>>" + response);
            //httpResult 只解析result字段
            HttpResponse httpResult = gson.fromJson(response, HttpResponse.class);
            //
//            if (httpResult.getCount() == 0) {
//                throw new ApiException(100);
//            }
            return gson.fromJson(response, type);


    }
}