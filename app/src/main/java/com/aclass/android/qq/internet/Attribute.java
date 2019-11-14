package com.aclass.android.qq.internet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    public static String QQ="1234567890";
    // 当前账号
    public static User currentAccount;
    // 当前账号头像
    public static Bitmap currentAccountProfilePhoto;
    public static List<Friend> friendList;
    public static Request friendVideoRequest;
    public static Request friendMessageRequest;
    public static Boolean isInVideo=false;
    public static byte[] video_bitmap;
    public static byte[] video_bitmap_send;

    public static Bitmap[][] emojiList;//表情包

    public static Thread mainMessageReceive;
    public static Thread restartMessageReceive;
}
