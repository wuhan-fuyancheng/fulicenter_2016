package com.example.administrator.fulicenter_2016.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.CartBean;
import com.example.administrator.fulicenter_2016.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CartAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CartBean> list;


    public CartAdapter(Context context, ArrayList<CartBean> list) {
        this.context = context;
        this.list = list;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    boolean isMore;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterHolder(View.inflate(context, R.layout.footer, null));
            return holder;
        } else if (viewType == I.TYPE_ITEM) {
            holder = new CartViewHolder(View.inflate(context, R.layout.item_cart, null));
            return holder;
        }
        return holder;


    }

    private String footsettext() {
        return isMore() ? "" : "";
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterHolder vh = (FooterHolder) holder;
            vh.tvFooter.setText(footsettext());
        } else {
            CartViewHolder vh = (CartViewHolder) holder;
            final CartBean cartBean = list.get(position);
            GoodsDetailsBean goods = (GoodsDetailsBean) cartBean.getGoods();
            if (goods != null) {
                ImageLoader.downloadImg(context, ((CartViewHolder) holder).ivCartGoodsimg, goods.getGoodsThumb());
                vh.tvCartGoodsname.setText(goods.getGoodsName());
                vh.tvCartGoodsprice.setText(goods.getCurrencyPrice());
            }
            Log.i("main", "Cart_cartBean_Count_" + cartBean.getCount());
            vh.tvCartNum.setText("(" + cartBean.getCount() + ")");
            vh.ivCartCheck.setChecked(cartBean.isChecked());
            vh.ivCartCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    cartBean.setChecked(b);
                    context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CartBean> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CartBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class FooterHolder extends ViewHolder {
        @BindView(R.id.tv_footer)
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class CartViewHolder extends ViewHolder {
        @BindView(R.id.tv_cart_num)
        TextView tvCartNum;
        @BindView(R.id.iv_cart_goodsimg)
        ImageView ivCartGoodsimg;
        @BindView(R.id.tv_cart_goodsname)
        TextView tvCartGoodsname;
        @BindView(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @BindView(R.id.tv_cart_del)
        ImageView tvCartDel;
        @BindView(R.id.layout_num)
        RelativeLayout layoutNum;
        @BindView(R.id.tv_cart_goodsprice)
        TextView tvCartGoodsprice;
        @BindView(R.id.iv_cart_check)
        CheckBox ivCartCheck;


        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
