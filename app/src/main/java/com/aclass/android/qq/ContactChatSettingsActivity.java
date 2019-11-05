package com.aclass.android.qq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aclass.android.qq.entity.Entity;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友聊天设置页面
 */
public class ContactChatSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_chat_settings);
        MyDateBase dateBase = new MyDateBase();
        Friend friend = dateBase.getFriend("1234567890", "0987654321");
        Log.d("GGGG", friend.toString());
    }
}
