package com.aclass.android.qq.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.aclass.android.qq.R;
import com.aclass.android.qq.tools.MyDateBase;

public class ProfileUtil {

    /**
     * 获取圆头像
     * @return 头像或者默认头像
     */
    public static Bitmap getRoundProfilePhoto(Context context, String number, MyDateBase dateBase){
        return GraphicsUtil.round(getProfilePhoto(context, number, dateBase));
    }

    public static Drawable getRoundProfilePhotoDrawable(Context context, String number, MyDateBase dateBase){
        return new BitmapDrawable(context.getResources(), getRoundProfilePhoto(context, number, dateBase));
    }

    /**
     * 获取头像
     * @return 头像或者默认头像
     */
    public static Bitmap getProfilePhoto(Context context, String number, MyDateBase dateBase){
        MyDateBase mDateBase = dateBase == null ? new MyDateBase() : dateBase;
        Bitmap profilePhoto = mDateBase.getImageByQQ(number);
        if (dateBase == null) mDateBase.Destory();
        if (profilePhoto == null) profilePhoto = getDefaultProfilePhoto(context);
        return profilePhoto;
    }

    public static Drawable getProfilePhotoDrawable(Context context, String number, MyDateBase dateBase){
        return new BitmapDrawable(context.getResources(), getProfilePhoto(context, number, dateBase));
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
    public static String getDisplayName(){
        return "";//todo
    }
}
