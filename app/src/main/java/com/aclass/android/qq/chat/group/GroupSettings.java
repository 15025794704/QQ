package com.aclass.android.qq.chat.group;

import com.aclass.android.qq.chat.Settings;
import com.aclass.android.qq.common.ProfileUtil;
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
        MyDateBase mDatabase = dateBase == null ? new MyDateBase() : dateBase;
        Qun groupAccount = mDatabase.getQun(groupNum);
        Member group = mDatabase.getMemberByQQAndID(groupNum, Attribute.currentAccount.getQQNum());
        if (dateBase == null) mDatabase.Destory();
        // todo 目前群功能仍欠缺，用于群设置页面展示
        if (group == null) group = fakeGroup(groupNum, Attribute.currentAccount.getQQNum());
        return new GroupSettings(groupAccount, group);
    }

    private static Member fakeGroup(String groupNum, String currentAccountNum){
        Member member = new Member();
        member.setQunID(groupNum);
        member.setMemberQQ(currentAccountNum);
        member.setNiCheng("Faker");
        member.setIsTop(0);
        member.setIsHide(0);
        member.setIsDisturb(0);
        return member;
    }

    public GroupSettings(Qun groupAccount, Member group){
        mDataGroupAccount = groupAccount;
        mDataGroup = group;
        number = groupAccount.getQunID();
        memberName = ProfileUtil.getGroupMemberDisplayName(number, group.getMemberQQ(), null, group, null);

        name = groupAccount.getName();
        remark = group.getNiCheng();
        hostNum = groupAccount.getHost();
        isPinnedTop = group.getIsTop() == 1;
        isDND = group.getIsDisturb() == 1;
        isHidden = group.getIsHide() == 1;
    }

    Qun toQun(){
        mDataGroupAccount.setName(name.isEmpty() ? null : name);
        mDataGroupAccount.setHost(hostNum.isEmpty() ? null : hostNum);
        return mDataGroupAccount;
    }

    Member toMember(){
        mDataGroup.setNiCheng(remark.isEmpty() ? null : remark);
        mDataGroup.setIsTop(isPinnedTop ? 1 : 0);
        mDataGroup.setIsDisturb(isDND ? 1 : 0);
        mDataGroup.setIsHide(isHidden ? 1 : 0);
        return mDataGroup;
    }
}
