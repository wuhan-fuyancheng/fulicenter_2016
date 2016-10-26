package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.AlbumsBean;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.bean.PropertiesBean;
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
    }

    private void setListener() {

    }

    private void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodDetails(result);
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
    public void onClick() {
    }
}
