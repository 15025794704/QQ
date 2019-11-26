package com.aclass.android.qq.chat.contact;

import com.aclass.android.qq.chat.Settings;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class ContactSettings extends Settings {
    /**
     * 分组
     */
    String groupTag;
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
        number = friend.getQQ2();
        name = friend.getBeiZhu();
        groupTag = friend.getQQgroup();
        isPinnedTop = friend.getIsTop() == 1;
        isDND = friend.getIsDisturb() == 1;
        isHidden = friend.getIsHide() == 1;
        isBlocked = friend.getIsBlocked() == 1;
        mData = friend;
    }

    Friend toFriend(){
        mData.setQQ2(number);
        mData.setBeiZhu(name);
        mData.setQQgroup(groupTag);
        mData.setIsTop(isPinnedTop ? 1 : 0);
        mData.setIsDisturb(isDND ? 1 : 0);
        mData.setIsHide(isHidden ? 1 : 0);
        mData.setIsBlocked(isBlocked ? 1 : 0);
        return mData;
    }
}
