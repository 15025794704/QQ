package com.aclass.android.qq.chat.contact;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.Entity;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友聊天设置页面
 */
public class ContactChatSettingsActivity extends GeneralActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_chat_settings);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                Friend friend = dateBase.getFriend("1234567890", "0987654321");
                dateBase.Destory();
                Log.d("GGGG", friend.toString());
            }
        }).start();
    }

    @Override
    protected void consumeInsets(Rect insets) {
    }
}
