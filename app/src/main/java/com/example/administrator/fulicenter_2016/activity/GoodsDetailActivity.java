package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.AlbumsBean;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.views.FlowIndicator;
import com.example.administrator.fulicenter_2016.views.SlideAutoLoopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                L.i("details","details="+result);
                if (result!=null){
                    showGoodDetails(result);
                }else {
                    finish();
                }
            }



            @Override
            public void onError(String error) {
                finish();
                L.i("details,error"+error);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean result) {
        tvGoodNameEnglish.setText(result.getGoodsEnglishName());
        tvGoodName.setText(result.getGoodsName());
        tvGoodPriceCurrent.setText(result.getCurrencyPrice());
        tvGoodPriceShop.setText(result.getShopPrice());
        salv.startPlayLoop(indicator,getAlbumImagUrl(result),getAlbumImgCount(result));
        wvGoodBrief.loadDataWithBaseURL(null,result.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private int getAlbumImgCount(GoodsDetailsBean result) {
        if (result.getProperArray()!=null&&result.getProperArray().length>0){
            return  result.getProperArray()[0].getAlbums().length;
        }
        else{
            return 0;}
    }

    private String[] getAlbumImagUrl(GoodsDetailsBean result) {
        String[] urls=new String[]{};
        if (result.getProperArray()!=null&&result.getProperArray().length>0){
            AlbumsBean[] albums=result.getProperArray()[0].getAlbums();
            urls=new String[albums.length];
            for (int i=0;i<albums.length;i++){
                urls[i]=albums[i].getImgUrl();
            }
        }
        return urls;
    }

    private void initView() {
    }
    @OnClick(R.id.backClickArea)
    public void onBackClick(){
        MFGT.finish(this);
    }
}
