package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/11/20.
 */

public class MsgList{


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQQFriend() {
        return QQFriend;
    }

    public void setQQFriend(String QQFriend) {
        this.QQFriend = QQFriend;
    }

    private String name;
    private String time;
    private String QQFriend;
    private int index;
    private boolean top;

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }


    public MsgList(String name, String time, String QQFriend, int index, boolean isTop) {
        this.name = name;
        this.time = time;
        this.QQFriend = QQFriend;
        this.index = index;
        this.top = isTop;
    }

    public MsgList(){}

}
