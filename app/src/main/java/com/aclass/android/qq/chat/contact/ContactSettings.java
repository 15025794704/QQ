package com.aclass.android.qq.chat.contact;

import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class ContactSettings {
    /**
     * 好友 QQ 号
     */
    String contactNum;
    /**
     * 备注
     */
    String contactName;
    /**
     * 分组
     */
    String groupTag;
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
    private Friend mData;

    public static ContactSettings get(String contactNum, MyDateBase dateBase){
        MyDateBase mDateBase = dateBase == null ? new MyDateBase() : dateBase;
        Friend friend = mDateBase.getFriend(Attribute.currentAccount.getQQNum(), contactNum);
        if (dateBase == null) mDateBase.Destory();
        if (friend == null) return null;
        return new ContactSettings(friend);
    }

    public ContactSettings(Friend friend){
        contactNum = friend.getQQ2();
        contactName = friend.getBeiZhu();
        groupTag = friend.getQQgroup();
        isPinnedTop = friend.getIsTop() == 1;
        isDND = friend.getIsDisturb() == 1;
        isBlocked = friend.getIsHide() == 1;
        mData = friend;
    }

    Friend toFriend(){
        mData.setQQ2(contactNum);
        mData.setBeiZhu(contactName);
        mData.setQQgroup(groupTag);
        mData.setIsTop(isPinnedTop ? 1 : 0);
        mData.setIsDisturb(isDND ? 1 : 0);
        mData.setIsHide(isBlocked ? 1 : 0);
        return mData;
    }
}
