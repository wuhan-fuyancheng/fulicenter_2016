package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.dao.UserDao;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    private final long sleepTime=2000;
    AppCompatActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user= FuLiCenterApplication.getUser();
                if (user==null){
                    UserDao dao=new UserDao(mContext);
                    user=dao.getUser("fuyancheng");
                    Log.i("main","spla_user="+user);
                }
                long start= System.currentTimeMillis();
                long costTime=System.currentTimeMillis()-start;
                if (sleepTime-costTime>0){
                    try {
                        Thread.sleep(sleepTime-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        },sleepTime);
    }
}
