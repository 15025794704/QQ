package com.aclass.android.qq.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.aclass.android.qq.R;

/**
 * Created by Administrator on 2019/10/26.
 */

public class ActivityOpreation {

    /**
     * 当前activity跳转到另一个activity，不finish当前activity
     * @param activity
     * @param clazz
     */
    public static void jumpActivity(Activity activity,Class<?> clazz){
        Intent intent=new Intent(activity,clazz);
        activity.startActivity(intent);
    }

    /**
     * 设置当前activity的状态栏背景颜色资源，以及显示的文字图标等颜色资源
     * @param activity
     * @param barColor
     * @param wordColor
     */
    public static void setStatusBar(Activity activity,int barColor,int wordColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(barColor));//设置状态栏颜色
            activity.getWindow().getDecorView().setSystemUiVisibility(wordColor);//实现状态栏图标和文字颜色为暗色
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色和占用状态栏
        }
    }
}
