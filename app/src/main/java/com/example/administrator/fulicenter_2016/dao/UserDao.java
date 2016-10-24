package com.example.administrator.fulicenter_2016.dao;

import android.content.ContentValues;
import android.content.Context;
import android.os.DropBoxManager;

import com.example.administrator.fulicenter_2016.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserDao {
    public static final String USER_TABLE_NAME="t_superwechat_user";
    public static final String USER_COLUMN_AVATAR_ID="m_user_avatar_id";
    public static final String USER_COLUMN_NICK="m_user_nick";
    public static final String USER_COLUMN_AVATAR_PATH="m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX="m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE="m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_LASTUPDATE_TIME="m_user_avatar_lastupdate_time";
    public static final String USER_COLUMN_NAME="m_user_name";
    public UserDao(Context context){
        DBmanager.getInstance().onInit(context);
    }
    public boolean saveUser(User user){
        return DBmanager.getInstance().saveUser(user);
    }
    public User getUser(String username){
        return DBmanager.getInstance().getUser(username);
    }
    public boolean updateUser(User user){
        return DBmanager.getInstance().updateUser(user);
    }
}
