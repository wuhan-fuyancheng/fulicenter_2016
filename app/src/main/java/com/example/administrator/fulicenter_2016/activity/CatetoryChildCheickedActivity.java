package com.example.administrator.fulicenter_2016.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.NewGoodsAdapter;
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

public class CatetoryChildCheickedActivity extends AppCompatActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.newgoods_recyclerView)
    RecyclerView newgoodsRecyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    GridLayoutManager glm;
    int childId;
    int pageId = 1;
    CatetoryChildCheickedActivity mContext;
    NewGoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    @BindView(R.id.bt_sort_price)
    Button btSortPrice;
    @BindView(R.id.bt_sort_addtime)
    Button btSortAddtime;

    //标志位
    boolean priceAdc=false;
    boolean addTimeAsc=false;
    int sortby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catetory_child_cheicked);
        ButterKnife.bind(this);
        childId = getIntent().getIntExtra("childId", 0);
        L.i("childId=" + childId);
        if (childId == 0) {
            finish();
        }
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new NewGoodsAdapter(mContext, mList);
        setListener();
        initData();
        initView();
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
    }

    private void initData() {
        downlodaCategoryChildChicked(I.ACTION_DOWNLOAD);
    }

    private void downlodaCategoryChildChicked(final int action) {
        NetDao.downloadCategoryChildCheicked(mContext, childId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.i("result=" + result);
                srl.setRefreshing(false);
                tvfresh.setVisibility(View.GONE);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (list.size()%2==1){
                        list.add(new NewGoodsBean());
                    }
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
                downlodaCategoryChildChicked(I.ACTION_PULL_DOWN);
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
                    downlodaCategoryChildChicked(I.ACTION_PULL_UP);
                }
            }
        });
    }

    @OnClick(R.id.backClickArea)
    public void onback() {
        MFGT.finish(this);
    }

    @OnClick({R.id.bt_sort_price, R.id.bt_sort_addtime})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.bt_sort_addtime:
                if (addTimeAsc){
                    sortby=I.SORT_BY_ADDTIME_ASC;
                    right=getResources().getDrawable(R.mipmap.arrow_order_up);
                }else {
                    sortby=I.SORT_BY_ADDTIME_DESC;
                    right=getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0,0,right.getIntrinsicWidth(),right.getIntrinsicHeight());
                btSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                addTimeAsc=!addTimeAsc;
                break;

            case R.id.bt_sort_price:
                if (priceAdc){
                    sortby=I.SORT_BY_PRICE_ASC;
                    right=getResources().getDrawable(R.mipmap.arrow_order_up);
                }else{
                    sortby=I.SORT_BY_PRICE_DESC;
                    right=getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0,0,right.getIntrinsicWidth(),right.getIntrinsicHeight());
                btSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                priceAdc=!priceAdc;
                break;
        }
        mAdapter.setSortBy(sortby);
    }
}
