package com.aclass.android.qq.common;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

public class ThemeUtil {

    public static @ColorInt int getColor(Context context, @AttrRes int attr){
        TypedValue resColor = new TypedValue();
        context.getTheme().resolveAttribute(attr, resColor, true);
        return resColor.data;
    }
}
