package com.example.administrator.fulicenter_2016.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.CategoryChildBean;
import com.example.administrator.fulicenter_2016.bean.CategoryGroupBean;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;
import com.example.administrator.fulicenter_2016.utils.MFGT;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mList;
    ArrayList<ArrayList<CategoryChildBean>> childList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> m, ArrayList<ArrayList<CategoryChildBean>> child) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        mList.addAll(m);
        childList = new ArrayList<>();
        childList.addAll(child);
    }

    @Override
    public int getGroupCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList
                .get(groupPosition) != null ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpan, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view== null) {
            view = View.inflate(mContext, R.layout.item_category, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            view.getTag();
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = (CategoryGroupBean) getGroup(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.ivCateImg, group.getImageUrl());
            holder.tvCateName.setText(group.getName());
            holder.ivCateXiala.setImageResource(isExpan ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_categorychild, null);
            holder=new ChildViewHolder(view);
            view.setTag(holder);
        }else {
            holder= (ChildViewHolder) view.getTag();
        }
        final CategoryChildBean child= (CategoryChildBean) getChild(groupPosition,childPosition);
        if (child!=null){
            ImageLoader.downloadImg(mContext,holder.ivCateChildImg,child.getImageUrl());
            holder.ivCateChildName.setText(child.getName());
            holder.layoutCateChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoCategoryChildChicked((Activity) mContext,child.getId());
                }
            });
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void initlist(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        mList.clear();
        mList.addAll(groupList);
        this.childList.addAll(childList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        @BindView(R.id.iv_cate_img)
        ImageView ivCateImg;
        @BindView(R.id.tv_cate_name)
        TextView tvCateName;
        @BindView(R.id.iv_cate_xiala)
        ImageView ivCateXiala;
        @BindView(R.id.layout_item_cate)
        RelativeLayout layoutItemCate;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_cateChild_img)
        ImageView ivCateChildImg;
        @BindView(R.id.iv_cateChild_name)
        TextView ivCateChildName;
        @BindView(R.id.layout_cateChild)
        RelativeLayout layoutCateChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
