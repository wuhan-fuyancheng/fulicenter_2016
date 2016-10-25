package com.example.administrator.fulicenter_2016.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.Result;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.dao.SharePrefrenceUtils;
import com.example.administrator.fulicenter_2016.dao.UserDao;
import com.example.administrator.fulicenter_2016.net.NetDao;
import com.example.administrator.fulicenter_2016.net.OkHttpUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.I;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;
import com.example.administrator.fulicenter_2016.utils.L;
import com.example.administrator.fulicenter_2016.utils.MFGT;
import com.example.administrator.fulicenter_2016.utils.OnSetAvatarListener;
import com.example.administrator.fulicenter_2016.utils.ResultUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonAtivity extends AppCompatActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.layout_me_userimg)
    RelativeLayout layoutMeUserimg;
    @BindView(R.id.layout_me_username)
    RelativeLayout layoutMeUsername;
    @BindView(R.id.tv_me_usernick)
    TextView tvMeUsernick;
    @BindView(R.id.layout_me_usernick)
    RelativeLayout layoutMeUsernick;
    @BindView(R.id.me_quit)
    Button meQuit;

    PersonAtivity mContext;
    @BindView(R.id.tv_me_username)
    TextView tvMeUsername;
    @BindView(R.id.im_me_useravatarimg)
    ImageView imMeUseravatarimg;

    User user = FuLiCenterApplication.getUser();
    String name=user.getMuserName();

    OnSetAvatarListener mOnSetAvatarListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_ativity);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        tvMeUsernick.setText(user.getMuserNick());
        tvMeUsername.setText(user.getMuserName());
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, imMeUseravatarimg);
        DisplayUtils.initBackWithTitle(mContext, "个人资料");
    }

    @OnClick({R.id.layout_me_userimg, R.id.layout_me_username, R.id.layout_me_usernick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_me_userimg:
                mOnSetAvatarListener=new OnSetAvatarListener(mContext,R.id.layout_upload_avatar,
                user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.layout_me_username:
                Toast.makeText(PersonAtivity.this, "用户名不可以被修改", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_me_usernick:
                LayoutInflater factory= LayoutInflater.from(PersonAtivity.this);
                final View view1=factory.inflate(R.layout.item_editdialog,null);
                final EditText editText= (EditText) view1.findViewById(R.id.et_dialog);
                editText.setText(FuLiCenterApplication.getUser().getMuserNick());
                new AlertDialog.Builder(PersonAtivity.this)
                        .setTitle("修改昵称")
                        .setView(view1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String nick=editText.getText().toString();
                                if (nick.isEmpty()){
                                    Toast.makeText(PersonAtivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                                    editText.requestFocus();
                                }else if (nick.equals(FuLiCenterApplication.getUser().getMuserNick())){
                                    Toast.makeText(PersonAtivity.this, "请不要输入同样的昵称", Toast.LENGTH_SHORT).show();
                                    editText.requestFocus();
                                }else {
                                    NetDao.updateNick(mContext, nick, name, new OkHttpUtils.OnCompleteListener<String>() {

                                        @Override
                                        public void onSuccess(String result) {
                                            Log.i("main", result);
                                            if (result == null) {
                                                Toast.makeText(PersonAtivity.this, "修改昵称失败", Toast.LENGTH_SHORT).show();
                                                editText.requestFocus();
                                            }
                                            Result result1 = ResultUtils.getResultFromJson(result, User.class);
                                            User userr = (User) result1.getRetData();
                                            UserDao dao = new UserDao(mContext);
                                            boolean isSuccess = dao.saveUser(userr);
                                            SharePrefrenceUtils.getInstence(mContext).saveUser(userr.getMuserName());
                                            if (isSuccess) {
                                                FuLiCenterApplication.setUser(userr);
                                                Toast.makeText(PersonAtivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                tvMeUsernick.setText(userr.getMuserNick());
                                            } else {
                                                Toast.makeText(PersonAtivity.this, "数据库异常", Toast.LENGTH_SHORT).show();
                                            }
                                            Log.i("main", "修改后的" + userr.getMuserNick());
                                            //tvMeUsernick.setText(user.getMuserNick());
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Toast.makeText(PersonAtivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("取消",null).create().show();

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("main","PersonActivityOnResume");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("main","onActivityResult,requestCode="+requestCode);
        if (resultCode!=RESULT_OK){
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,imMeUseravatarimg);
        if (requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }
    }

    private void updateAvatar() {
        final ProgressDialog pd=new ProgressDialog(mContext);
        pd.setMessage("图像上传中");
        pd.show();
        File file=new File(OnSetAvatarListener.getAvatarPath(mContext,user.getMavatarPath()+"/"
        +user.getMuserName()+user.getMavatarSuffix()));
        Log.i("main",file.getAbsolutePath());
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("main","avatarimg_result="+result);
                Result result1=ResultUtils.getResultFromJson(result,user.getClass());
                if (result1==null){
                    Toast.makeText(PersonAtivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                }else{
                    User u= (User) result1.getRetData();
                    if (result1.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        Toast.makeText(PersonAtivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,imMeUseravatarimg);
                    }else {
                        Toast.makeText(PersonAtivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    @OnClick(R.id.me_quit)
    public void onQuickClick() {
        if (user!=null){
            SharePrefrenceUtils.getInstence(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
        MFGT.finish(this);
    }
}
