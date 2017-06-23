package com.example.guosenlin.rxjavaretrofit.http;

import com.example.guosenlin.rxjavaretrofit.model.Gank;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by guosenlin on 2017/6/23.
 */

public interface ApiService {

    @GET("Android/{month}/{day}")
    Observable<HttpResponse<List<Gank>>> getGankList(@Path("month") int month, @Path("day") int day);
}
