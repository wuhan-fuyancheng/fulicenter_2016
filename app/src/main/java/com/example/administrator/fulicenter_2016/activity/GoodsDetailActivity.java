package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.AlbumsBean;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.bean.MessageBean;
import com.example.administrator.fulicenter_2016.bean.PropertiesBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.views.FlowIndicator;
import com.example.administrator.fulicenter_2016.views.SlideAutoLoopView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;

    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    GoodsDetailActivity mContext;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.iv_good_coll)
    ImageView ivGoodColl;

    GoodsDetailsBean goods;
    User user = FuLiCenterApplication.getUser();
    boolean isCollect;
    @BindView(R.id.iv_good_share)
    ImageView ivGoodShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        Log.i("main", goodsId + "GoodsDetailActivity");
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        initView();
        initData();
        setListener();
        ShareSDK.initSDK(mContext, "185d0d0a9eb8a");
    }

    private void setListener() {

    }

    private void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodDetails(result);
                    goods = result;
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void showGoodDetails(GoodsDetailsBean result) {
        tvGoodNameEnglish.setText(result.getGoodsEnglishName());
        tvGoodName.setText(result.getGoodsName());
        tvGoodPriceCurrent.setText(result.getCurrencyPrice());
        wvGoodBrief.loadDataWithBaseURL(null, result.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
        /*tvGoodPriceShop.setText(result.getShopPrice());*/

        ArrayList<String> imageUrls = new ArrayList<>();
        PropertiesBean[] properties = result.getProperArray();
        for (int i = 0; i < properties.length; i++) {
            PropertiesBean bean = properties[i];
            AlbumsBean[] albumsBeens = bean.getAlbums();
            for (AlbumsBean albumsBean : albumsBeens) {
                imageUrls.add(albumsBean.getImgUrl());
            }
        }
        String[] urls = (String[]) imageUrls.toArray(new String[imageUrls.size()]);
        salv.startPlayLoop(indicator, urls, urls.length);
    }

    private void initView() {


    }

    @OnClick(R.id.backClickArea)
    public void onBackClick() {
        MFGT.finish(this);
    }

    @OnClick(R.id.iv_good_coll)
    public void onCollectClick() {
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            if (!isCollect) {
                addCollectgoods();
            } else {
                NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = false;
                            Toast.makeText(GoodsDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            isCollect = true;
                            Toast.makeText(GoodsDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        updateGoodsCollectStatus();
                    }

                    @Override
                    public void onError(String error) {
                        isCollect = true;
                        updateGoodsCollectStatus();
                    }
                });
            }
        }
    }

    private void addCollectgoods() {
        if (user != null) {
            NetDao.addCollectgoods(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollect = true;
                        Toast.makeText(GoodsDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        updateGoodsCollectStatus();
                    } else {
                        isCollect = false;
                        Toast.makeText(GoodsDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        updateGoodsCollectStatus();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    Toast.makeText(GoodsDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    updateGoodsCollectStatus();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        isCollected();
    }

    private void isCollected() {
        if (user != null) {
            NetDao.updateCollected(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollect = true;
                    } else {
                        isCollect = false;
                    }
                    updateGoodsCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    updateGoodsCollectStatus();
                }
            });
        }
    }

    private void updateGoodsCollectStatus() {
        if (isCollect) {
            ivGoodColl.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivGoodColl.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.iv_good_share)
    public void onShareClick() {
        Toast.makeText(GoodsDetailActivity.this, "123", Toast.LENGTH_SHORT).show();
        if (goods != null) {
            showShare();
        }
    }

    private void showShare() {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();

        oks.setTitle(goods.getGoodsName());//标题
        oks.setText(goods.getGoodsBrief());//正文
        // oks.setComment("我是评论文本");
        oks.setTitleUrl(goods.getShareUrl()); //分享地址
        PropertiesBean[] properties = goods.getProperArray();
        AlbumsBean[] albumsBeens = properties[0].getAlbums();
        Log.i("main", albumsBeens[0].getThumbUrl());
        oks.setImageUrl(I.DOWNLOAD_IMG_URL + albumsBeens[0].getImgUrl()); //分享界面的图片
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if ("Qzone".equals(platform.getName())) {
                    //  paramsToShare.setTitle("分享fyc空间");
                }
            }
        });
        oks.show(mContext);
    }

    @OnClick(R.id.iv_good_cart)
    public void onCartClick() {
        if(user==null){
            MFGT.gotoLoginActivity(mContext);
        }else {
            goCart();
        }
    }

    private void goCart() {

    }
}
