package com.example.administrator.fulicenter_2016.utils;

import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;


public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(FuLiCenterApplication.application,msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(FuLiCenterApplication.application,msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(FuLiCenterApplication.application.getResources().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(FuLiCenterApplication.application.getResources().getString(rId));
    }
}
