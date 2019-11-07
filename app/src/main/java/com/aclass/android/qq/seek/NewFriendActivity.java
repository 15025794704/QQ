package com.aclass.android.qq.seek;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityNewFriendBinding;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * 添加好友，填写验证消息
 */
public class NewFriendActivity extends GeneralActivity {
    public static String ARG_CONTACT = "Contact";

    private ActivityNewFriendBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityNewFriendBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());

        Intent intent = getIntent();
        User contact = intent.getParcelableExtra(ARG_CONTACT);
        final String name = contact.getNiCheng();
        final String num = contact.getQQNum();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.newFriendName.setText(name + " " + num);
                    }
                });
            }
        }).start();

        mViews.newFriendToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViews.newFriendToolbarSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Friend friend = new Friend();
                        friend.setQQ1("1234567890");
                        friend.setQQ2(num);
                        friend.setIsAgree(0);
                        MyDateBase dateBase = new MyDateBase();
                        int result = dateBase.insertEntity(friend);
                        dateBase.Destory();
                    }
                }).start();
                finish();
            }
        });
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar tb = mViews.newFriendToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
    }
}
