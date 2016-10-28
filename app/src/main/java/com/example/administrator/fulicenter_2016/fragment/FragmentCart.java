package com.example.administrator.fulicenter_2016.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.activity.PayActivity;
import com.example.administrator.fulicenter_2016.adapter.CartAdapter;
import com.example.administrator.fulicenter_2016.bean.CartBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.utils.ResultUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCart extends Fragment {
    View view;
    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.cart_recyclerView)
    RecyclerView cartRecView;
    @BindView(R.id.srl_cart)
    SwipeRefreshLayout srlCart;
    CartAdapter mAdapter;
    MainActivity mContext;
    ArrayList<CartBean> mList;
    LinearLayoutManager llm;
    @BindView(R.id.layout_tv_nothing)
    TextView layoutTvNothing;
    @BindView(R.id.layout_cart_price)
    RelativeLayout layoutCartPrice;
    @BindView(R.id.tv_cart_currentprice)
    TextView tvCartCurrentprice;
    @BindView(R.id.tv_cart_rankprice)
    TextView tvCartRankprice;
    UpdateCartReceiver mReceiver;
    UpdateCartReceiver mre;

    public FragmentCart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getActivity();
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        initData();
        initView();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownloadNewGoods();// 下拉刷新
        IntentFilter filter = new IntentFilter();
        filter.addAction(I.BROADCAST_UPDATA_CART);
        filter.addAction(I.BROADCAST_DELETE_CART);
        mReceiver = new UpdateCartReceiver();
        mContext.registerReceiver(mReceiver, filter);//注册广播
    }

    private void setPullDownloadNewGoods() {
        srlCart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlCart.setRefreshing(true);
                tvfresh.setVisibility(View.VISIBLE);
                downloadCartdata(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        downloadCartdata(I.ACTION_DOWNLOAD);
    }

    private void downloadCartdata(final int action) {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.downloadCartdata(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        Log.i("main", "CartResult_" + s);
                        srlCart.setRefreshing(false);
                        tvfresh.setVisibility(View.GONE);
                        ArrayList<CartBean> list = ResultUtils.getCartFromJson(s);
                        if (list != null && list.size() > 0) {
                            Log.i("main", "cartList_" + list);
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.initData(mList);
                            setCartLayout(true);
                        } else {
                            setCartLayout(false);
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    setCartLayout(false);
                    srlCart.setRefreshing(false);
                    tvfresh.setVisibility(View.GONE);
                    // Toast.makeText(MainActivity.class, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initView() {
        srlCart.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        llm = new LinearLayoutManager(mContext);
        cartRecView.setLayoutManager(llm);
        cartRecView.setHasFixedSize(true); //可否修复大小
        cartRecView.setAdapter(mAdapter);
        // setCartLayout(false);
    }

    private void setCartLayout(boolean hasCart) {
        layoutCartPrice.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        layoutTvNothing.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        cartRecView.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        sumPice();
    }

    private void sumPice() {
        int sumPrice = 0;
        int rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                if (c.isChecked()) {
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            tvCartCurrentprice.setText("合计：￥" + Double.valueOf(rankPrice));
            tvCartRankprice.setText("节省：￥" + Double.valueOf(sumPrice - rankPrice));
        } else {
            tvCartCurrentprice.setText("合计：￥0");
            tvCartRankprice.setText("节省：￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);  //截取字符串
        return Integer.valueOf(price);
    }

    @OnClick(R.id.bt_cart_buy)
    public void onbuyClick() {
        MFGT.gotoPayActivity(mContext);
    }



    class UpdateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case I.BROADCAST_UPDATA_CART:
                    sumPice();
                    break;
                case I.BROADCAST_DELETE_CART:
                    if (mList.size() == 0) {
                        setCartLayout(false);
                        Log.i("main", "fragmentCart_listsize_");
                    }
            }
            //  setCartLayout(false);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);  //销毁广播
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadCartdata(I.ACTION_DOWNLOAD);
    }
}
