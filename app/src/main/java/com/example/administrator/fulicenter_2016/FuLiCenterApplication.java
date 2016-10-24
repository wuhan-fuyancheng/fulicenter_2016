package com.example.administrator.fulicenter_2016;

import android.app.Application;

import com.example.administrator.fulicenter_2016.bean.User;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application{
    public static FuLiCenterApplication application;
    private static FuLiCenterApplication instance;
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }

    //判断个人中心是否有用户
    String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        instance=this;
    }

    public FuLiCenterApplication() {
        instance=this;
    }
    public static FuLiCenterApplication getInstance(){
        if (instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }
}
