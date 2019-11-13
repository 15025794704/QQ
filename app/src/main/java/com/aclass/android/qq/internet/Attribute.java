package com.aclass.android.qq.internet;

import android.graphics.Bitmap;

import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2019/11/8.
 *
 * APP共享的数据存放
 * 可以自行添加数据项
 */

public class Attribute {
    public static String QQ="1505249457";
    // 当前账号
    public static User currentAccount;
    // 当前账号头像
    public static Bitmap currentAccountProfilePhoto;
    public static List<Friend> friendList;
    public static Request friendVideoRequest;
    public static Boolean isInVideo;
    public static Bitmap video_bitmap;
    public static Bitmap video_bitmap_send;

    public static Bitmap[][] emojiList;//表情包
}
