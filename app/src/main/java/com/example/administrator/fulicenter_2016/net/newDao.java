package com.example.administrator.fulicenter_2016.net;


import android.content.Context;

import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.utils.I;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewDao {
    public static void downloadNewGoods(Context context, int pageId,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils utils=new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
}
