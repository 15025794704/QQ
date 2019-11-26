package com.aclass.android.qq.chat.group;

import com.aclass.android.qq.chat.Settings;
import com.aclass.android.qq.entity.Member;
import com.aclass.android.qq.entity.Qun;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class GroupSettings extends Settings {
    /**
     * 群主 QQ 号
     */
    String hostNum;
    /**
     * 个人群昵称
     */
    String memberName;
    private Qun mDataGroupAccount;
    private Member mDataGroup;

    public static GroupSettings get(String groupNum, MyDateBase dateBase){
        MyDateBase mDateBase = dateBase == null ? new MyDateBase() : dateBase;
        Qun groupAccount = mDateBase.getQun(groupNum);
        Member group = mDateBase.getMemberByQQAndID(groupNum, Attribute.currentAccount.getQQNum());
        if (dateBase == null) mDateBase.Destory();
        if (group == null) return null;
        return new GroupSettings(groupAccount, group);
    }

    public GroupSettings(Qun groupAccount, Member group){
        number = groupAccount.getQunID();
        name = groupAccount.getName();
        hostNum = groupAccount.getHost();
        memberName = group.getNiCheng();
        isPinnedTop = group.getIsTop() == 1;
        isDND = group.getIsDisturb() == 1;
        isHidden = group.getIsHide() == 1;
        mDataGroupAccount = groupAccount;
        mDataGroup = group;
    }

    Qun toQun(){
        mDataGroupAccount.setQunID(number);
        mDataGroupAccount.setName(name);
        mDataGroupAccount.setHost(hostNum);
        return mDataGroupAccount;
    }

    Member toMember(){
        mDataGroup.setNiCheng(memberName);
        mDataGroup.setIsTop(isPinnedTop ? 1 : 0);
        mDataGroup.setIsDisturb(isDND ? 1 : 0);
        mDataGroup.setIsHide(isHidden ? 1 : 0);
        return mDataGroup;
    }
}
