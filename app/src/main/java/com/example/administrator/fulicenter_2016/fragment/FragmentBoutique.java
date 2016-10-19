package com.example.administrator.fulicenter_2016.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.BoutiqueAdapter;
import com.example.administrator.fulicenter_2016.bean.BoutiqueBean;
import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBoutique extends Fragment {


    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.newgoods_recyclerView)
    RecyclerView newgoodsRecyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    BoutiqueAdapter mAdapter;
    MainActivity mContext;
    ArrayList<BoutiqueBean> mList;
    LinearLayoutManager llm;
    public FragmentBoutique() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_fragment_boutique, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getContext();
        mList=new ArrayList<>();
        mAdapter=new BoutiqueAdapter(mContext,mList);
        initData();  //list
        initView();  //adapter
        setListener();  //上拉下拉
        return layout;
    }

    private void setListener() {
        setPullDownloadNewGoods();// 下拉刷新
    }

    private void setPullDownloadNewGoods() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvfresh.setVisibility(View.VISIBLE);
                downloadBoutiquedata(I.ACTION_PULL_DOWN);
            }
        });
    }
    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        llm=new LinearLayoutManager(mContext);
        newgoodsRecyclerView.setLayoutManager(llm);
        newgoodsRecyclerView.setHasFixedSize(true); //可否修复大小
        newgoodsRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        downloadBoutiquedata(I.ACTION_DOWNLOAD);
    }

    private void downloadBoutiquedata(final int action) {
        NetDao.downloadBoutique(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                srl.setRefreshing(false);
                tvfresh.setVisibility(View.GONE);
               if (result!=null&&result.length>0){
                ArrayList<BoutiqueBean> list=ConvertUtils.array2List(result);
                   if (action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                       mAdapter.initData(list);
                   }else {
                       mAdapter.addData(list);
                   }
                   if (list.size()<I.PAGE_SIZE_DEFAULT){
                       mAdapter.setMore(false);
                   }else {mAdapter.setMore(true);}
               }
            }
            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvfresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.i("error:"+error);
            }
        });

    }
}
