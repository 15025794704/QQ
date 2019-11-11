package com.aclass.android.qq.common;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.aclass.android.qq.R;

/**
 * Created by Administrator on 2019/10/26.
 */

public class MyButtonOperation {

    /**
     * 设置imagebutton按动改变button背景图
     *设置imageButton按下时的图片填充资源 和 弹起时的图片填充资源
     * @param context  “填this即可”
     * @param imageButton
     * @param srcDown  按下的资源文件
     * @param srcUp  弹起的资源文件
     */
    public static void changeImageButton(final Context context, ImageButton imageButton, final int srcDown, final int srcUp) {
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageButton) v).setImageDrawable(context.getResources().getDrawable(srcDown));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    SystemClock.sleep(100);
                    ((ImageButton) v).setImageDrawable(context.getResources().getDrawable(srcUp));
                }
                return false;
            }
        });
    }

//    /**错误方法不可用
//     * ImageButton快捷设置要执行的方法，没有返回值
//     * @param btn
//     * @param obj
//     * @param methodName
//     * @param args
//     */
//    private static void setOnClick(ImageButton btn,final Object obj, final String methodName,final Object[] args){
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyReflect.invoke(obj,methodName,args);
//            }
//        });
//    }

//    /**错误方法不可用
//     * Button快捷设置要执行的方法，没有返回值
//     * @param btn
//     * @param obj
//     * @param methodName
//     * @param args
//     */
//    private static void setOnClick(Button btn,final Object obj, final String methodName,final Object[] args){
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyReflect.invoke(obj,methodName,args);
//            }
//        });
//    }
}
