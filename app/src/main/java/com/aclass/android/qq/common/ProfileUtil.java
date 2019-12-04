package com.aclass.android.qq.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.aclass.android.qq.R;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.Member;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class ProfileUtil {

    /**
     * 获取圆头像
     * @return 头像或者默认头像
     */
    public static Bitmap getRoundProfilePhoto(Context context, String number, MyDateBase database){
        return GraphicsUtil.round(getProfilePhoto(context, number, database));
    }

    public static Drawable getRoundProfilePhotoDrawable(Context context, String number, MyDateBase database){
        return new BitmapDrawable(context.getResources(), getRoundProfilePhoto(context, number, database));
    }

    /**
     * 获取头像
     * @return 头像或者默认头像
     */
    public static Bitmap getProfilePhoto(Context context, String number, MyDateBase database){
        boolean isSelf = number == null;
        boolean isSelfReady = isSelf && Attribute.isAccountInitialized;
        MyDateBase mDatabase = (database == null && !isSelfReady) ? new MyDateBase() : database;
        Bitmap profilePhoto = isSelfReady ? Attribute.currentAccountProfilePhoto : mDatabase.getImageByQQ(number);
        if (database == null && !isSelfReady) mDatabase.Destory();
        if (profilePhoto == null) profilePhoto = getDefaultProfilePhoto(context);
        return profilePhoto;
    }

    public static Drawable getProfilePhotoDrawable(Context context, String number, MyDateBase database){
        return new BitmapDrawable(context.getResources(), getProfilePhoto(context, number, database));
    }

    /**
     * 获取默认头像
     * @return 默认头像
     */
    public static Bitmap getDefaultProfilePhoto(Context context){
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_photo_default);
    }

    public static Drawable getDefaultProfilePhotoDrawable(Context context){
        return new BitmapDrawable(context.getResources(), getDefaultProfilePhoto(context));
    }

    /**
     * 获取显示名称
     * @return 账号昵称或者备注
     */
    public static String getDisplayName(String number, String otherNumber, MyDateBase database, Friend friend, User user){
        String displayName;
        MyDateBase mDatabase = database == null && friend == null ? new MyDateBase() : database;
        displayName = (friend == null ? mDatabase.getFriend(number, otherNumber) : friend).getBeiZhu();
        if (displayName == null) {
            if (mDatabase == null && user == null) mDatabase = new MyDateBase();
            displayName = (user == null ? mDatabase.getUser(otherNumber) : user).getNiCheng();
        }
        if (database == null && mDatabase != null) mDatabase.Destory();
        return displayName;
    }

    /**
     * 获取显示名称
     * @return 账号昵称或者群成员昵称
     */
    public static String getGroupMemberDisplayName(String groupNumber, String otherNumber, MyDateBase database, Member member, User user){
        String displayName;
        MyDateBase mDatabase = database == null && member == null ? new MyDateBase() : database;
        displayName = (member == null ? mDatabase.getMemberByQQAndID(groupNumber, otherNumber) : member).getNiCheng();
        if (displayName == null) {
            if (mDatabase == null && user == null) mDatabase = new MyDateBase();
            displayName = (user == null ? mDatabase.getUser(otherNumber) : user).getNiCheng();
        }
        if (database == null && mDatabase != null) mDatabase.Destory();
        return displayName;
    }
}
