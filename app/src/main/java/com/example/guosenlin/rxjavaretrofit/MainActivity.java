package com.example.guosenlin.rxjavaretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.guosenlin.rxjavaretrofit.http.ApiManager;
import com.example.guosenlin.rxjavaretrofit.model.Gank;
import com.example.guosenlin.rxjavaretrofit.subscribers.ProgressSubscriber;
import com.example.guosenlin.rxjavaretrofit.subscribers.SubscriberOnNextListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    private SubscriberOnNextListener getTopMovieOnNext;

    @Bind(R.id.tvMain)
    TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getTopMovieOnNext = new SubscriberOnNextListener<List<Gank>>() {
            @Override
            public void onNext(List<Gank> subjects) {
                 tvMain.setText(subjects == null?"请求失败":"请求成功：数据个数我为："+ subjects.size());
            }
        };



    }
     @OnClick(R.id.btnStart)
     public void onClick(){
         getMovie();
     }

    //进行网络请求
    private void getMovie(){
        ApiManager.getInstance(this).getGankList(new ProgressSubscriber(getTopMovieOnNext, MainActivity.this), 10, 1);
    }
}

