package com.example.administrator.fulicenter_2016.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.Result;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;

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

    RegisterActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext=this;
        initView();
        setListener();
        initData();
    }

    private void initView() {
    }

    private void initData() {

    }

    private void setListener() {
        DisplayUtils.initBackWithTitle(this,"账户注册");  //将back与title写在了一起但互不影响
    }

    @OnClick(R.id.bt_register_login)
    public void onRegisterClick() {
        name = etUsername.getText().toString().trim();
        nick = etUsernick.getText().toString().trim();
        passwdtwo = etUserpasswordtwo.getText().toString().trim();
        password = etUserpassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "输入用户名为空", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();//请求获得焦点（光标）
            return;
        }else if (!name.matches("[a-zA-Z]\\w{5,15}")){
            Toast.makeText(RegisterActivity.this, "请以英文字母开头，输入6-16位用户名", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return;
        }else if (TextUtils.isEmpty(nick)){
            Toast.makeText(RegisterActivity.this, "昵称为空", Toast.LENGTH_SHORT).show();
            etUsernick.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
            etUserpassword.requestFocus();
            return;
        }else if (TextUtils.isEmpty(passwdtwo)){
            Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            etUserpasswordtwo.requestFocus();
            return;
        }else if (!password.equals(passwdtwo)){
            Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            etUserpasswordtwo.requestFocus();
            return;
        }else {
            regist();
        }
    }

    private void regist() {
      /*  final ProgressDialog pd=new ProgressDialog(mContext);
        pd.setMessage("注册中");
        pd.show();*/
        NetDao.register(mContext, name, nick, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                L.i("RegisterResut"+result);
                if (result==null){
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }else {
                    if (!result.isRetMsg()){ //得到boolean类型的retmsg，true表示成功 false表示已存在
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        etUsername.requestFocus();
                    }else {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,name));
                      //  pd.dismiss();
                        MFGT.finish(mContext);
                    }
                }
            }

            @Override
            public void onError(String error) {
             //   pd.dismiss();
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
