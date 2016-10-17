package com.example.administrator.fulicenter_2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulicenter_2016.fragment.FragmentNewGoods;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Fragment[] fragments;
    FragmentNewGoods fragmentNewGoods;
    @BindView(R.id.mainRB1)
    RadioButton mainRB1;
    @BindView(R.id.mainRB2)
    RadioButton mainRB2;
    @BindView(R.id.mainRB3)
    RadioButton mainRB3;
    @BindView(R.id.mainRB4)
    RadioButton mainRB4;

    int index;
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
    }

    private void initFragment() {
        fragments=new Fragment[5];
        fragmentNewGoods=new FragmentNewGoods();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,fragmentNewGoods)
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
                index=4;
                break;
        }
        setRadioButtonStatus(); //互斥
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
}
