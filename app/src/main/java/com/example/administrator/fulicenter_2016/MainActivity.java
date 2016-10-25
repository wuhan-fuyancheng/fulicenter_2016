package com.example.administrator.fulicenter_2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulicenter_2016.fragment.FragmentBoutique;
import com.example.administrator.fulicenter_2016.fragment.FragmentCategory;
import com.example.administrator.fulicenter_2016.fragment.FragmentMe;
import com.example.administrator.fulicenter_2016.fragment.FragmentNewGoods;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Fragment[] fragments;
    FragmentNewGoods fragmentNewGoods;
    FragmentBoutique fragmentBoutique;
    FragmentCategory fragmentCategory;
    FragmentMe mFragmentMe;
    @BindView(R.id.mainRB1)
    RadioButton mainRB1;
    @BindView(R.id.mainRB2)
    RadioButton mainRB2;
    @BindView(R.id.mainRB3)
    RadioButton mainRB3;
    @BindView(R.id.mainRB4)
    RadioButton mainRB4;
    int index;
    int currentIndex=0;
    RadioButton[] rbs;
    @BindView(R.id.mainRB5)
    RadioButton mainRB5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment();
        setListener();
    }

    private void setListener() {
    }

    private void initFragment() {
        fragments=new Fragment[5];
        fragmentNewGoods=new FragmentNewGoods();
        fragmentBoutique=new FragmentBoutique();
        fragmentCategory=new FragmentCategory();
        mFragmentMe=new FragmentMe();
        fragments[0]=fragmentNewGoods;
        fragments[1]=fragmentBoutique;
        fragments[2]=fragmentCategory;
        fragments[4]=mFragmentMe;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,fragmentNewGoods)
                .add(R.id.fragment_container,fragmentBoutique)
                .add(R.id.fragment_container,fragmentCategory)
                .hide(fragmentCategory)
                .hide(fragmentBoutique)
                .show(fragmentNewGoods)
                .commit();
    }

    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = mainRB1;
        rbs[1] = mainRB2;
        rbs[2] = mainRB3;
        rbs[3] = mainRB4;
        rbs[4] = mainRB5;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {//找下标、通过下标找对应按钮
            case R.id.mainRB1:
                index=0;
                break;
            case R.id.mainRB2:
                index=1;
                break;
            case R.id.mainRB3:
                index=2;
                break;
            case R.id.mainRB4:
                index=3;
                break;
            case R.id.mainRB5:
                if (FuLiCenterApplication.getInstance().getUser()==null){
                    MFGT.gotoLoginActivity(this);
                }
                else {
                index=4;}
                break;
        }
        setFragment();
        //互斥
    }

    private void setFragment() {
        if (index!=currentIndex){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()){
                ft.add(R.id.fragment_container,fragments[index]);
            }
            ft.show(fragments[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex=index;
    }

    private void setRadioButtonStatus() {
        for (int i=0;i<rbs.length;i++){
            if (i==index){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("main","onresume");
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("main","onActivityResult");
        if(requestCode== I.REQUEST_CODE_LOGIN&&FuLiCenterApplication.getUser()!=null){
            index=4;
        }
    }
}
