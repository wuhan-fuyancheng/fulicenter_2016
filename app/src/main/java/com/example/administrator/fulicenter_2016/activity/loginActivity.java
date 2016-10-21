package com.example.administrator.fulicenter_2016.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_userpassword)
    EditText etUserpassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setListener();
        initData();
    }

    private void initData() {

    }

    private void setListener() {
        DisplayUtils.initBack(this);
    }

    @OnClick(R.id.bt_register)
    public void onClick() {
        MFGT.gotoRegisterActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //得到返回的数据name
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode== I.REQUEST_CODE_REGISTER){
            String name=data.getStringExtra(I.User.USER_NAME);
            L.i("username:"+name);
            etUsername.setText(name);
        }
    }
}
