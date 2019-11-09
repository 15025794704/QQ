package com.aclass.android.qq.internet;

import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.Request;

import java.util.List;

/**
 * Created by Administrator on 2019/11/8.
 *
 * APP共享的数据存放
 * 可以自行添加数据项
 */

public class Attribute {
    public static String QQ="1505249457";
    public static List<Friend> friendList;
    public static Request friendVideoRequest;
    public static Boolean isInVideo;
    public static byte[] video_bitmap;
    public static byte[] video_bitmap_send;
}
