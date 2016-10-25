package com.example.administrator.fulicenter_2016.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.activity.BoutiqueChildctivity;
import com.example.administrator.fulicenter_2016.activity.CatetoryChildCheickedActivity;
import com.example.administrator.fulicenter_2016.activity.GoodsDetailActivity;
import com.example.administrator.fulicenter_2016.activity.LoginActivity;
import com.example.administrator.fulicenter_2016.activity.PersonAtivity;
import com.example.administrator.fulicenter_2016.activity.RegisterActivity;
import com.example.administrator.fulicenter_2016.bean.BoutiqueBean;
import com.example.administrator.fulicenter_2016.bean.CategoryChildBean;

import java.io.Serializable;
import java.util.ArrayList;


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
    public static void gotoBoutiqueChildActivity(Activity context, BoutiqueBean boutiqueBean){

        Intent intent=new Intent();
        intent.setClass(context, BoutiqueChildctivity.class);
        intent.putExtra("ilu",boutiqueBean);  //序列化
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoCategoryChildChicked(Activity context, int childId, String groupname, ArrayList<CategoryChildBean> list){
        Intent intent=new Intent();
        intent.setClass(context, CatetoryChildCheickedActivity.class);
        intent.putExtra("list",list);
        intent.putExtra("name",groupname);
        intent.putExtra("childId",childId);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoLoginActivity(Activity context){
        Intent intent=new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoRegisterActivity(Activity context){
       /* startActivity(context,RegisterActivity.class);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);*/
        Intent intent=new Intent();
        intent.setClass(context,RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);
    }

    private static void startActivityForResult(Activity context, Intent intent, int requestCode) {  //将带返回值得到跳转的方法提取出来
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoPersonActivity(Activity context){
        Intent intent=new Intent();
        intent.setClass(context,PersonAtivity.class);
        context.startActivity(intent);
    }


}
