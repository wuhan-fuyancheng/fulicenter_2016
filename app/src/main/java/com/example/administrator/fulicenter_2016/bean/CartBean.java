package com.example.administrator.fulicenter_2016.bean;

import com.example.administrator.fulicenter_2016.utils.I;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CartBean implements Serializable{

    /**
     * id : 35
     * userName : a952702
     * goodsId : 7677
     * goods : null
     * count : 2
     * isChecked : false
     * checked : false
     */

    private int id;
    private String userName;
    private int goodsId;
    private int count;
    private boolean isChecked;
    private boolean checked;
    private GoodsDetailsBean goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public GoodsDetailsBean getGoods() {
        return goods;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public CartBean() {
    }

    public void setGoods(GoodsDetailsBean goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", count=" + count +
                ", isChecked=" + isChecked +
                ", checked=" + checked +
                ", goods=" + goods +
                '}';
    }
}
