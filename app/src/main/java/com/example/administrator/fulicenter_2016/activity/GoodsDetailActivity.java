package com.example.administrator.fulicenter_2016.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.views.FlowIndicator;
import com.example.administrator.fulicenter_2016.views.SlideAutoLoopView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailActivity extends AppCompatActivity {
    int goodsId;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        Log.i("main", goodsId + "");
        if (goodsId==0){
            finish();
        }
        mContext=this;
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
                L.i("details="+result);
                if (result!=null){
                    showGoodDetails(result);
                }else {
                    finish();
                }
            }



            @Override
            public void onError(String error) {
                finish();
                L.e("details,error"+error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean result) {
        tvGoodNameEnglish.setText(result.getGoodsEnglishName());
        tvGoodName.setText(result.getGoodsName());
        tvGoodPriceCurrent.setText(result.getCurrencyPrice());
        tvGoodPriceShop.setText(result.getShopPrice());
    }

    private void initView() {
    }
}
