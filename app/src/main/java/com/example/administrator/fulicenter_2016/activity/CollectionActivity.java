package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.CollectionAdapter;
import com.example.administrator.fulicenter_2016.bean.CollectBean;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.views.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionActivity extends AppCompatActivity {
    GridLayoutManager glm;
    CollectionAdapter mAdapter;
    CollectionActivity mContext;
    ArrayList<CollectBean> mList;
    User user = FuLiCenterApplication.getUser();
    int pageId = 1;
    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.collection_rcv)
    RecyclerView collRcv;
    @BindView(R.id.srl_collectionGoods)
    SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        mContext = this;
        mList=new ArrayList<>();
        mAdapter=new CollectionAdapter(mList,mContext);
        initData();
        initView();
        setListener();
    }

    private void setListener() {
        setUpDownloadNewGoods(); //上拉加载
        setPullDownloadNewGoods();
    }

    private void setUpDownloadNewGoods() {
        collRcv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == collRcv.SCROLL_STATE_IDLE
                        && mAdapter.ismore()) {
                    pageId++;
                    downloadCollection(I.ACTION_PULL_UP);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setPullDownloadNewGoods() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId=1;
                srl.setRefreshing(true);
                tvfresh.setVisibility(View.VISIBLE);
                downloadCollection(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {

        if (user!= null) {
            downloadCollection(I.ACTION_DOWNLOAD);
        }
    }

            private void downloadCollection(final int action) {
                NetDao.downloadCollect(mContext, user.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        Log.i("main","collection-result-");
                        srl.setRefreshing(false);
                        tvfresh.setVisibility(View.GONE);
                        if (result!=null&&result.length>0){
                            ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                            if (action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                                mAdapter.initData(list);}
                            if (action==I.ACTION_PULL_UP){
                                mAdapter.addData(list);
                            }
                            if (list.size()<I.PAGE_SIZE_DEFAULT){
                                mAdapter.setIsmore(false);
                            }
                            else {mAdapter.setIsmore(true);}
                        }
                    }

                    @Override
                    public void onError(String error) {
                        tvfresh.setVisibility(View.GONE);
                        CommonUtils.showShortToast(error);
                        L.i("error:"+error);
                    }
                });
            }

            private void initView() {
                DisplayUtils.initBackWithTitle(mContext, "收藏的宝贝");
                srl.setColorSchemeColors(
                        getResources().getColor(R.color.google_blue),
                        getResources().getColor(R.color.google_green),
                        getResources().getColor(R.color.google_red),
                        getResources().getColor(R.color.google_yellow)
                );
                glm=new GridLayoutManager(mContext, I.COLUM_NUM);
                collRcv.setLayoutManager(glm);
                collRcv.setHasFixedSize(true); //可否修复大小
                collRcv.setAdapter(mAdapter);
                collRcv.addItemDecoration(new SpaceItemDecoration(12));
            }

            @Override
            protected void onResume() {
                Log.i("main","CollectionActivity_onResume");
                super.onResume();
                initData();
                initView();
                setListener();
            }
        }
