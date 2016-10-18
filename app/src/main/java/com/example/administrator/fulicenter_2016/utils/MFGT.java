package com.example.administrator.fulicenter_2016.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.activity.GoodsDetailActivity;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoGoodsDetailsActivity(Activity context, int goodId){

        Intent intent=new Intent();
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodId);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
