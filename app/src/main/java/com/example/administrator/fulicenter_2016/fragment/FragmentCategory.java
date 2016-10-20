package com.example.administrator.fulicenter_2016.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.adapter.CategoryAdapter;
import com.example.administrator.fulicenter_2016.bean.CategoryChildBean;
import com.example.administrator.fulicenter_2016.bean.CategoryGroupBean;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.ConvertUtils;
import com.example.administrator.fulicenter_2016.utils.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategory extends Fragment {


    @BindView(R.id.elv)
    ExpandableListView elv;
    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;
    Context context;
    int childId;
    int groupCount=0;

    public FragmentCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_category, container, false);
        context=getActivity();
        ButterKnife.bind(this, view);
        groupList=new ArrayList<>();
        childList=new ArrayList<>();
        mAdapter=new CategoryAdapter(context,groupList,childList);
        initData();
        initView();
        return view;
    }

    private void initView() {
        elv.setGroupIndicator(null);  //设置箭头
        elv.setAdapter(mAdapter);
    }

    private void initData() {
        downloadCategoryGoup();
    }

    private void downloadCategoryGoup() {
        NetDao.downloadCategoryGroup(context, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result!=null&&result.length>0){
                    ArrayList<CategoryGroupBean> list= ConvertUtils.array2List(result);
                    groupList.addAll(list);
                    for (int i=0;i<list.size();i++){
                        childList.add(new ArrayList<CategoryChildBean>());
                        childId=list.get(i).getId();
                        downloadCategoryChild(childId,i);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void downloadCategoryChild(int childId, final int index) {
        NetDao.downloadCategoryChild(context, childId, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override

            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                L.i("downloadChild,result="+result.toString());
                if (result!=null&&result.length>0){
                    ArrayList<CategoryChildBean>  childlistt=ConvertUtils.array2List(result);
                    childList.set(index,childlistt);
                    L.i("childlist="+childlistt.size());
                }
                if (groupCount==groupList.size()){
                    mAdapter.initlist(groupList,childList);
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
