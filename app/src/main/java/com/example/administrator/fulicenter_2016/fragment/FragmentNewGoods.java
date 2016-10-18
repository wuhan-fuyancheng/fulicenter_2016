package com.example.administrator.fulicenter_2016.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.NewGoodsAdapter;
import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.net.NewDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.views.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewGoods extends Fragment {
    GridLayoutManager glm;
    MainActivity mContext;
    NewGoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int pageId = 1;
    @BindView(R.id.tvfresh)
    TextView tvfresh;
    @BindView(R.id.newgoods_recyclerView)
    RecyclerView newgoodsRecyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    public FragmentNewGoods() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getContext();
        mList=new ArrayList<>();
        mAdapter=new NewGoodsAdapter(mContext,mList);
        initView();
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setUpDownloadNewGoods(); //上拉加载
        setPullDownloadNewGoods();// 下拉刷新
    }

    private void setUpDownloadNewGoods() {
        newgoodsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition=glm.findLastVisibleItemPosition();
                if (lastPosition>=mAdapter.getItemCount()-1&&newState==newgoodsRecyclerView.SCROLL_STATE_IDLE
                        &&mAdapter.ismore()){
                    pageId++;
                    downloadnewgoodsdata(I.ACTION_PULL_UP);
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
                downloadnewgoodsdata(I.ACTION_PULL_DOWN);
            }
        });
    }


    private void downloadnewgoodsdata(final int action){
        NewDao.downloadNewGoods(mContext, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.i("result="+result);
                srl.setRefreshing(false);
                tvfresh.setVisibility(View.GONE);
                if (result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
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
    private void initData() {
        downloadnewgoodsdata(I.ACTION_DOWNLOAD);
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        glm=new GridLayoutManager(mContext, I.COLUM_NUM);
        newgoodsRecyclerView.setLayoutManager(glm);
        newgoodsRecyclerView.setHasFixedSize(true); //可否修复大小
        newgoodsRecyclerView.setAdapter(mAdapter);
        newgoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }

}
