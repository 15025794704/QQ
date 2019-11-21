package com.aclass.android.qq.main.contacts;

import android.graphics.Bitmap;

/**
 * Created by 24015 on 2019/11/14.
 */

public class ContentInfo {
    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    private String QQ;//QQ号
    private String beiZhu;//备注
    private String qianming;//个性签名
    private Bitmap icon;//头像

    public int getIsHide() {
        return isHide;
    }

    public void setIsHide(int isHide) {
        this.isHide = isHide;
    }

    private int isHide;//是否在线



    public String getBeiZhu() {
        return beiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        this.beiZhu = beiZhu;
    }

    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
