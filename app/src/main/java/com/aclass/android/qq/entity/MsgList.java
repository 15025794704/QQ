package com.aclass.android.qq.entity;

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

    public String getQQFriend() {
        return QQFriend;
    }

    public void setQQFriend(String QQFriend) {
        this.QQFriend = QQFriend;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MsgList(){}

    public MsgList(String QQFriend, String name, String time, int index, boolean isTop) {
        this.QQFriend = QQFriend;
        this.name = name;
        this.time = time;
        this.index = index;
        this.isTop = isTop;
    }

    String name;
    String time;
    String QQFriend;
    int index;
    boolean isTop=false;

}
