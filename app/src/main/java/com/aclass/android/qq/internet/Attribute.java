package com.aclass.android.qq.internet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.MsgList;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/11/8.
 *
 * APP共享的数据存放
 * 可以自行添加数据项
 */

public class Attribute {
    public static String QQ;
    // 当前账号
    public static User currentAccount;
    // 当前账号头像
    public static Bitmap currentAccountProfilePhoto;
    // 当前账号是否完成初始化
    public static boolean isAccountInitialized = false;
    public static Map<String,Friend> friendList;
    //所有qq对应的信息
    public static Map<String,User> userInfoList;
    //所有qq的头像
    public static Map<String,Bitmap> userHeadList;

    //点击同意好友,列表更新
    public static int agreeFriendClick=0;


    //好友消息列表
    public static List<MsgList> msgList;

    //手机屏幕对象
    public  static Screen screen;

    //*****************************《《《《《《《《《《《《《《《
    //消息、接收线程专用版
    public static Request friendVideoRequest;
    public static Request friendMessageRequest;
    public static ArrayList<Message> msgArrayList;
    public static String insertQQview;//当前所处qq聊天窗口的好友qq
    public static Boolean isInVideo=false;
    public static byte[] video_bitmap;
    public static byte[] video_bitmap_send;

    public static Bitmap[][] emojiList;//表情包

    public static Thread mainMessageReceive;
    public static Thread restartMessageReceive;
    //*****************************》》》》》》》》》》》》》》》》
}
