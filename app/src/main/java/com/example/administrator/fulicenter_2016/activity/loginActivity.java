package com.example.administrator.fulicenter_2016.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.Result;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.dao.UserDao;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.CommonUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.utils.ResultUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsernamee;
    @BindView(R.id.et_userpassword)
    EditText etUserpasswordd;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;

    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext=this;
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
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            L.i("username:" + name);
            etUsernamee.setText(name);
        }
    }

    @OnClick(R.id.bt_login)
    public void onloginClick() {
        String name=etUsernamee.getText().toString();
        String password=etUserpasswordd.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            etUsernamee.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            etUserpasswordd.requestFocus();
        }
        login(name,password);
    }

    private void login(String name,String password) {
        NetDao.login(mContext, name, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Result resultt= ResultUtils.getResultFromJson(result,User.class);
                Log.i("main","loginresult："+result);
               Log.i("main",resultt.toString());
                if (result==null){
                    CommonUtils.showLongToast("登录失败");
                    etUserpasswordd.requestFocus();
                }else {
                    if (resultt.isRetMsg()){
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        User user= (User) resultt.getRetData();
                        UserDao dao=new UserDao(mContext);
                        boolean isSuccess =dao.saveUser(user);
                        if (isSuccess){
                            FuLiCenterApplication.setUser(user);
                            MFGT.finish(mContext);
                        }else {
                            Toast.makeText(LoginActivity.this, "数据库异常", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (resultt.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
                            etUsernamee.requestFocus();
                        }else if (resultt.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            Toast.makeText(LoginActivity.this,"密码错误", Toast.LENGTH_LONG).show();
                            etUserpasswordd.requestFocus();
                        }else {
                            Toast.makeText(LoginActivity.this,"登录失败", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(error);
            }
        });
    }
}
