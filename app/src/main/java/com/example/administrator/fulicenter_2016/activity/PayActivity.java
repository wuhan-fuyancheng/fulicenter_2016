package com.example.administrator.fulicenter_2016.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.CartBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ResultUtils;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends AppCompatActivity implements PaymentHandler {

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
    String carIds = "";
    String[] ids = new String[]{};
    User user = FuLiCenterApplication.getUser();
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    int rankPrice = 0;

    private static String URL = "http://218.244.151.190/demo/charge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        initData();
        initView();
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
                Log.i("main", "pay_result_" + result);
                ArrayList<CartBean> list = ResultUtils.getCartFromJson(result);
                /*for(CartBean b:list){
                Log.i("main",b.toString());
                }*/
                if (list == null || list.size() == 0) {
                    finish();
                } else {
                    if (mList != null) {
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
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                Log.i("main", c.toString());
                for (String id : ids) {
                    if (id.equals(c.getId() + "")) {
                        Log.i("main", id + "");
                        rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                        Log.i("main", "pay_price_" + rankPrice);
                    }
                }
            }
        }
        tvPayPrice.setText("合计：￥" + Double.valueOf(rankPrice));
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);  //截取字符串
        return Integer.valueOf(price);
    }

    @OnClick(R.id.bt_pay_buy)
    public void onBuyClick() {
        String username=etPayUsername.getText().toString();
        if (TextUtils.isEmpty(username)){
            etPayUsername.setError("收货人不能为空");
            etPayUsername.requestFocus();
            return;
        }
        String phone=etPayPhone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            etPayPhone.setError("手机号码不能为空");
            etPayPhone.requestFocus();
            return;
        }
        if (!phone.matches("[\\d]{11}")){
            etPayPhone.setError("手机号码格式错误");
            etPayPhone.requestFocus();
            return;
        }
        String area=spinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)){
            Toast.makeText(PayActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
            return;
        }
        String street=etPayAdress.getText().toString();
        if (TextUtils.isEmpty(street)){
            etPayAdress.setError("街道地址不能为空");
            etPayAdress.requestFocus();
            return;
        }
        gotoStatements();

    }

    private void gotoStatements() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());


        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount",rankPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                   Log.i("main","handlePaymentResult_result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
