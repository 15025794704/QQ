package com.aclass.android.qq.chat.contact;

import com.aclass.android.qq.chat.Settings;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class ContactSettings extends Settings {
    /**
     * 屏蔽
     */
    boolean isBlocked;
    private Friend mData;

    public static ContactSettings get(String contactNum, MyDateBase database){
        MyDateBase mDatabase = database == null ? new MyDateBase() : database;
        Friend friend = mDatabase.getFriend(Attribute.currentAccount.getQQNum(), contactNum);
        if (database == null) mDatabase.Destory();
        if (friend == null) return null;
        return new ContactSettings(friend);
    }

    public ContactSettings(Friend friend){
        mData = friend;
        number = friend.getQQ2();
        name = ProfileUtil.getDisplayName(friend.getQQ1(), number, null, friend, null);

        remark = friend.getBeiZhu();
        isPinnedTop = friend.getIsTop() == 1;
        isDND = friend.getIsDisturb() == 1;
        isHidden = friend.getIsHide() == 1;
        isBlocked = friend.getIsBlocked() == 1;
    }

    Friend toFriend(){
        mData.setBeiZhu(remark.isEmpty() ? null : remark);
        mData.setIsTop(isPinnedTop ? 1 : 0);
        mData.setIsDisturb(isDND ? 1 : 0);
        mData.setIsHide(isHidden ? 1 : 0);
        mData.setIsBlocked(isBlocked ? 1 : 0);
        return mData;
    }
}
