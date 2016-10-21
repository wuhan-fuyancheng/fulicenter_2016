package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_usernick)
    EditText etUsernick;
    @BindView(R.id.et_userpassword)
    EditText etUserpassword;
    @BindView(R.id.et_userpasswordtwo)
    EditText etUserpasswordtwo;
    @BindView(R.id.bt_register_login)
    Button btRegisterLogin;


    String name;
    String nick;
    String password;
    String passwdtwo;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        tvCommonTitle.setText("账户注册");
    }

    private void initData() {

    }

    private void setListener() {
        name = etUsername.getText().toString();
        nick = etUsernick.getText().toString();
        passwdtwo = etUserpasswordtwo.getText().toString();
        password = etUserpassword.getText().toString();
        DisplayUtils.initBackWithTitle(this,"账户登录");
    }

    @OnClick(R.id.bt_register_login)
    public void onRegisterClick() {
    }




}
