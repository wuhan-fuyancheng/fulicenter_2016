package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.CartBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ResultUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends AppCompatActivity {

    @BindView(R.id.et_pay_username)
    EditText etPayUsername;
    @BindView(R.id.et_pay_phone)
    EditText etPayPhone;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.et_pay_adress)
    EditText etPayAdress;
    @BindView(R.id.bt_pay_buy)
    Button btPayBuy;
    PayActivity mContext;
    ArrayList<CartBean> mList;
    String carIds ="";
    String[] ids = new String[]{};
    User user = FuLiCenterApplication.getUser();
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        initData();
        initView();
    }

    private void initView() {
        DisplayUtils.initBackWithTitle(mContext, "商品支付");
    }

    private void initData() {
        carIds = getIntent().getStringExtra(I.Cart.ID);
        Log.i("main", "cartIds_" + carIds);
        if (carIds == null || carIds.equals("") || user == null) {
            finish();
        }
        ids = carIds.split(",");
        geOrderList();
    }

    private void geOrderList() {
        NetDao.downloadCartdata(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("main","pay_result_"+result);
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(result);
                /*for(CartBean b:list){
                Log.i("main",b.toString());
                }*/
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    if (mList!=null) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    sumPrice();
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        int rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                Log.i("main",c.toString());
                for (String id : ids) {
                    if (id.equals(c.getId()+"")) {
                        Log.i("main",id+"");
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                        Log.i("main","pay_price_"+rankPrice);
                    }
                }
            }
        }
        tvPayPrice.setText("合计：￥"+Double.valueOf(rankPrice));
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);  //截取字符串
        return Integer.valueOf(price);
    }
}
