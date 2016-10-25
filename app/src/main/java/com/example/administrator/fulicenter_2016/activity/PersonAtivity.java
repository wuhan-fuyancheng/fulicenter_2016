package com.example.administrator.fulicenter_2016.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter_2016.FuLiCenterApplication;
import com.example.administrator.fulicenter_2016.R;
import com.example.administrator.fulicenter_2016.bean.User;
import com.example.administrator.fulicenter_2016.dao.SharePrefrenceUtils;
import com.example.administrator.fulicenter_2016.utils.DisplayUtils;
import com.example.administrator.fulicenter_2016.utils.ImageLoader;
import com.example.administrator.fulicenter_2016.utils.MFGT;

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
                break;
            case R.id.layout_me_username:
                break;
            case R.id.layout_me_usernick:
                break;
        }
    }

    @OnClick(R.id.me_quit)
    public void onQuickClick() {
        if (user!=null){
            SharePrefrenceUtils.getInstence(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
        finish();
    }
}
