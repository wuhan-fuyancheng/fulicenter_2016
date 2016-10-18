package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.utils.I;

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        goodsId=getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID,0);
        Log.i("main",goodsId+"");
    }
}
