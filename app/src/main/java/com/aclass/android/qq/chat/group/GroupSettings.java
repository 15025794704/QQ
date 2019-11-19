package com.aclass.android.qq.chat.group;

import com.aclass.android.qq.entity.Member;
import com.aclass.android.qq.entity.Qun;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class GroupSettings {
    /**
     * 群号
     */
    String groupNum;
    /**
     * 群名称
     */
    String groupName;
    /**
     * 群主 QQ 号
     */
    String hostNum;
    /**
     * 群昵称
     */
    String memberName;
    /**
     * 聊天置顶
     */
    boolean isPinnedTop;
    /**
     * 新消息勿扰
     */
    boolean isDND;
    /**
     * 屏蔽
     */
    boolean isBlocked;

    public static GroupSettings get(String groupNum, MyDateBase dateBase){
        MyDateBase mDateBase = dateBase == null ? new MyDateBase() : dateBase;
        Qun groupAccount = mDateBase.getQun(groupNum);
        Member group = mDateBase.getMemberByQQAndID(groupNum, Attribute.currentAccount.getQQNum());
        if (dateBase == null) mDateBase.Destory();
        return new GroupSettings(groupAccount, group);
    }

    public GroupSettings(Qun groupAccount, Member group){
        groupNum = groupAccount.getQunID();
        groupName = groupAccount.getName();
        hostNum = groupAccount.getHost();
        memberName = group.getNiCheng();
        isPinnedTop = group.getIsTop() == 1;
        isDND = group.getIsDisturb() == 1;
        isBlocked = group.getIsHide() == 1;
    }
}
