package com.example.administrator.fulicenter_2016.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.NewGoodsBean;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewGoodsAdapter extends RecyclerView.Adapter {
    Context context;
    List<NewGoodsBean> mlist;

    public NewGoodsAdapter(Context context, List<NewGoodsBean> mlist) {
        this.context = context;
        this.mlist = new ArrayList<>();
        this.mlist.addAll(mlist);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterHolder(View.inflate(context, R.layout.footer, null));
            return holder;
        }
        holder = new NewGoodsHolder(View.inflate(context, R.layout.item_newgoods, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){
            FooterHolder vh= (FooterHolder) holder;
            if (position==getItemCount()-1){
            vh.tvFooter.setText("没有更多数据了");}
            else {
                vh.tvFooter.setText("加载更多数据");
            }

        }else {
            NewGoodsHolder vh= (NewGoodsHolder) holder;
            NewGoodsBean goods=mlist.get(position);
            ImageLoader.downloadImg(context,vh.ivItemNewgoods,goods.getGoodsThumb());
            vh.tvItemNewgoodsname.setText(goods.getGoodsName());
            vh.tvItemNewgoodsjiage.setText(goods.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() + 1 : 1;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (list!=null){
            mlist.clear();
        }
        mlist.addAll(list);
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

    static class NewGoodsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_item_newgoods)
        ImageView ivItemNewgoods;
        @BindView(R.id.tv_item_newgoodsname)
        TextView tvItemNewgoodsname;
        @BindView(R.id.tv_item_newgoodsjiage)
        TextView tvItemNewgoodsjiage;

        NewGoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
