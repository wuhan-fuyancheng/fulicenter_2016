package com.example.administrator.fulicenter_2016.net;


import android.content.Context;

import com.example.administrator.fulicenter_2016.bean.BoutiqueBean;
import com.example.administrator.fulicenter_2016.bean.CategoryChildBean;
import com.example.administrator.fulicenter_2016.bean.CategoryGroupBean;
import com.example.administrator.fulicenter_2016.bean.CollectBean;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.bean.MessageBean;
import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.bean.Result;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.MD5;

import java.io.File;

public class NetDao {
    public static void downloadNewGoods(Context context, int pageId,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
    public static void downloadGoodsDetail(Context context,int goodsId,OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener){
        OkHttpUtils<GoodsDetailsBean> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String .valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }
    public static void downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener){
        OkHttpUtils<BoutiqueBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }
    public static void downloadBoutiqueChild(Context context,int catId,int pageId,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public static void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener){
        OkHttpUtils<CategoryGroupBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }
    public static void downloadCategoryChild(Context context, int parentId,OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener){
        OkHttpUtils<CategoryChildBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
    public static void downloadCategoryChildCheicked(Context context,int catId,int pageId,
                                             OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public static void register(Context context,String name,String nick,String password,
                                OkHttpUtils.OnCompleteListener<Result> listner){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,name)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listner);
    }
    public static void login(Context context, String name, String password, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,name)
                .addParam(I.User.PASSWORD,MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }
    public static void updateNick(Context context, String nick, String username, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void updateAvatar(Context context, String name, File file, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,name)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void getCollectsCount(Context context, String name, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,name)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    public static void downloadCollect(Context context, String username, int pageId, OkHttpUtils.OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(listener);
    }
}
