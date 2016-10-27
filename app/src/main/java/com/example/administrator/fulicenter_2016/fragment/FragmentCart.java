package com.example.administrator.fulicenter_2016.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.CartAdapter;
import com.example.administrator.fulicenter_2016.bean.CartBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.I;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public FragmentCart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mContext= (MainActivity) getActivity();
        mList=new ArrayList<>();
        mAdapter=new CartAdapter(mContext,mList);
        initData();
        initView();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownloadNewGoods();// 下拉刷新
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
        User user= FuLiCenterApplication.getUser();
        if (user!=null) {
            NetDao.downloadCartdata(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        Log.i("main", "CartResult_" + result);
                        srlCart.setRefreshing(false);
                        tvfresh.setVisibility(View.GONE);
                        if (result != null && result.length > 0) {
                            ArrayList<CartBean> list = ConvertUtils.array2List(result);
                            mAdapter.initData(list);
                        }
                    }
                }

                @Override
                public void onError(String error) {
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
    }

}
