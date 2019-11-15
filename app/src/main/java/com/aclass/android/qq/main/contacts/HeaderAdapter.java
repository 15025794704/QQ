package com.aclass.android.qq.main.contacts;

import android.view.View;

/**
 * Created by 24015 on 2019/11/14.
 */

public interface HeaderAdapter {
    /*
    * adapter接口，列表必须实现此接口
    * */
    public static final int PINNED_HEADER_GONE=0;
    public static final int PINNED_HEADER_VISIBLE=1;
    public static final int PINNED_HEADER_PUSHED_UP=2;
    /*
    * 获取Header的状态
    * @param groupPosition
    * @param childPosition
    * @return PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP 当中之中的一个
    * */

   public  int getHeaderState(int groupPosition, int childPosition);

    /*
    * 配置Header，让Header知道显示的内容
    * @param header
    * @param groupPosition
    * @param childPosition
    * @param alpha
    * */

    public void configureHeader(View header, int groupPosition, int childPosition, int alpha);

    /*
    * 设置组按下的状态
    * @param groupPosition
    * @param status
    * */
    public void setGroupClickStatus(int groupPosition, int status);

    /*
    * 获取组按下的状态
    * @param groupPosition
    * @return
    * */

    public int getGroupClickStatus(int groupPosition);




}
