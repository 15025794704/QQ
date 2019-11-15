package com.aclass.android.qq.main.contacts;

import java.util.List;

/**
 * Created by 24015 on 2019/11/14.
 */

public class GroupTitleInfo {
    private String QQGroupName;
    private List<ContentInfo> Info;

    public String getQQGroupName() {
        return QQGroupName;
    }

    public void setQQGroupName(String QQGroupName) {
        this.QQGroupName = QQGroupName;
    }

    public List<ContentInfo> getInfo() {
        return Info;
    }

    public void setInfo(List<ContentInfo> Info) {
        this.Info = Info;
    }
}
