package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.NewGoodsAdapter;
import com.example.administrator.fulicenter_2016.bean.BoutiqueBean;
import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.views.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoutiqueChildctivity extends AppCompatActivity {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.newgoods_recyclerView)
    RecyclerView newgoodsRecyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    BoutiqueBean bb;
    BoutiqueChildctivity mContext;
    ArrayList<NewGoodsBean> mList;
    NewGoodsAdapter mAdapter;
    GridLayoutManager glm;
    int pageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_childctivity);
        ButterKnife.bind(this);
        bb = (BoutiqueBean) getIntent().getSerializableExtra("ilu");
        Log.i("main", bb.getId() + "精选id");
        if (bb == null) {
            finish();
        }
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new NewGoodsAdapter(mContext, mList);
        setListener();
        initData();
        initView();
    }

    private void setListener() {
        setUpDownloadNewGoods(); //上拉加载
        setPullDownloadNewGoods();// 下拉刷新
    }

    private void setPullDownloadNewGoods() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 1;
                srl.setRefreshing(true);
                tvfresh.setVisibility(View.VISIBLE);
                downloadnewgoodsdata(I.ACTION_PULL_DOWN);
            }
        });
    }




    private void setUpDownloadNewGoods() {
        newgoodsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == newgoodsRecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.ismore()) {
                    pageId++;
                    downloadnewgoodsdata(I.ACTION_PULL_UP);
                }
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
                glm = new GridLayoutManager(mContext, I.COLUM_NUM);
                newgoodsRecyclerView.setLayoutManager(glm);
                newgoodsRecyclerView.setHasFixedSize(true); //可否修复大小
                newgoodsRecyclerView.setAdapter(mAdapter);
                newgoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
                tvCommonTitle.setText(bb.getTitle());
            }

            private void initData() {
                downloadnewgoodsdata(I.ACTION_DOWNLOAD);
            }

            private void downloadnewgoodsdata(final int action) {
                NetDao.downloadBoutiqueChild(mContext, bb.getId(), pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        L.i("result=" + result);
                        srl.setRefreshing(false);
                        tvfresh.setVisibility(View.GONE);
                        if (result != null && result.length > 0) {
                            ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                            if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                                mAdapter.initData(list);
                            }
                            if (action == I.ACTION_PULL_UP) {
                                mAdapter.addData(list);
                            }
                            if (list.size() < I.PAGE_SIZE_DEFAULT) {
                                mAdapter.setIsmore(false);
                            } else {
                                mAdapter.setIsmore(true);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        tvfresh.setVisibility(View.GONE);
                        CommonUtils.showShortToast(error);
                        L.i("error:" + error);
                    }
                });
            }
    @OnClick(R.id.backClickArea)
    public void onClick(){
        MFGT.finish(this);
    }
        }

