package com.example.administrator.fulicenter_2016.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.MainActivity;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.activity.CollectionActivity;
import com.example.administrator.fulicenter_2016.bean.CategoryChildBean;
import com.example.administrator.fulicenter_2016.bean.CollectBean;
import com.example.administrator.fulicenter_2016.bean.MessageBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;
import com.example.administrator.fulicenter_2016.utils.MFGT;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CollectionAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;
    boolean ismore;


    public boolean ismore() {
        return ismore;
    }

    public void setIsmore(boolean ismore) {
        this.ismore = ismore;
    }

    public CollectionAdapter(ArrayList<CollectBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterHolder(View.inflate(mContext, R.layout.footer, null));
            return holder;
        }
        holder = new CollectionGoodsHolder(View.inflate(mContext, R.layout.item_collectiongoods, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterHolder vh = (FooterHolder) holder;
            vh.tvFooter.setText(footsettext());
        } else {
            CollectionGoodsHolder vh = (CollectionGoodsHolder) holder;
            final CollectBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext, vh.ivCollectGoods, goods.getGoodsThumb());
            vh.tvCollectionItemName.setText(goods.getGoodsName());

            vh.layoutCollectionGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoGoodsDetailsActivity((Activity) mContext,goods.getGoodsId());
                }
            });
            vh.layoutCollectionGoods.setTag(goods);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (list != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }


    static class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer)
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private int footsettext() {
        return ismore() ? R.string.load_more : R.string.no_more;
    }


    class CollectionGoodsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_collectionGoods)
        RelativeLayout layoutCollectionGoods;
        @BindView(R.id.iv_collect_goods)
        ImageView ivCollectGoods;
        @BindView(R.id.iv_collection_delete)
        ImageView ivCollectionDelete;
        @BindView(R.id.tv_collection_item_name)
        TextView tvCollectionItemName;

        CollectionGoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.iv_collection_delete)
        public void onClick() {
            String username= FuLiCenterApplication.getUser().getMuserName();
            final CollectBean goods= (CollectBean) layoutCollectionGoods.getTag();
            NetDao.deleteCollect(mContext, username, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result!=null&&result.isSuccess()){
                        mList.remove(goods);
                        notifyDataSetChanged();
                    }else {
                        CommonUtils.showLongToast(result!=null?result.getMsg():"收藏失败");
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("main","collectdelete_"+error);
                }
            });
        }


    }






}
