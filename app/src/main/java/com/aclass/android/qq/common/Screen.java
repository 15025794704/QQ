package com.aclass.android.qq.common;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

/**
 * Created by Administrator on 2019/10/26.
 */

public class Screen {
    //屏幕宽
    private int posWidth;
    //屏幕高
    private int posHeight;


    private Screen(){

    }

    public Screen(Activity activity){
        // 通过Activity类中的getWindowManager()方法获取窗口管理，再调用getDefaultDisplay()方法获取获取Display对象
        Display display = activity.getWindowManager().getDefaultDisplay();
        // 方法一(推荐使用)使用Point来保存屏幕宽、高两个数据
        Point outSize = new Point();
        // 通过Display对象获取屏幕宽、高数据并保存到Point对象中
        display.getSize(outSize);
        // 从Point对象中获取宽、高
        posWidth = outSize.x;
        posHeight = outSize.y;
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public int getposWidth() {
        return posWidth;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public int getposHeight() {
        return posHeight;
    }

    /**
     * 获取控件在整个屏幕的位置
     * @param view
     * @return
     */
    public int[] getPostion(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
     //   System.out.println("x:"+x+"y:"+y);  30,74
    //    System.out.println("图片各个角Left："+t.getLeft()+"Right："+t.getRight()+"Top："+t.getTop()+"Bottom："+t.getBottom());
                               //30,100,36,100
        return location;
    }

}
