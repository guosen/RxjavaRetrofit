package com.example.guosenlin.rxjavaretrofit.http;

import android.content.Context;

import com.example.guosenlin.rxjavaretrofit.model.Gank;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by guosenlin on 2017/6/23.
 */

public class ApiManager {

    public static final String HEADER_ACCEPT_JSON = "application/json";
    public static final String HEADER_ACCEPT      = "Accept";
    private        ApiService mApiService;
    private static ApiManager sInstance;
    private Context mContext;

    public static final String BASE_URL = "http://Gank.io/api/data/";

    private static final int DEFAULT_TIMEOUT = 5;


    public ApiManager(Context context) {
        mContext=context;
        getService();

    }

    private ApiService createService(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                //modify by zqikai 20160317 for 对http请求结果进行统一的预处理 GosnResponseBodyConvert
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService = retrofit.create(ApiService.class);
        return mApiService;
    }

    public ApiService getService() {
        if (mApiService == null) {
            synchronized (this) {
                if (mApiService == null)
                    mApiService = createService();
            }
        }
        return mApiService;
    }

    public static ApiManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ApiManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    private class HttpResultFunc<T> implements Func1<HttpResponse<T>, T> {

        @Override
        public T call(HttpResponse<T> httpResult) {
            if (httpResult  == null) {
                throw new ApiException(100);
            }
            return httpResult.results;
        }
    }

    public void getGankList(Subscriber<List<Gank>> subscriber, int month, int day){
        Observable observable = mApiService.getGankList(month, day)
                .map(new HttpResultFunc<List<Gank>>());

        toSubscribe(observable, subscriber);
    }
}
