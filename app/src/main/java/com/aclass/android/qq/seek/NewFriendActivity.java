package com.aclass.android.qq.seek;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.databinding.ActivityNewFriendBinding;
import com.aclass.android.qq.entity.User;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                User contact = intent.getParcelableExtra(ARG_CONTACT);
                final String name = contact.getNiCheng();
                final String num = contact.getQQNum();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.newFriendName.setText(name + " " + num);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        Toolbar tb = mViews.newFriendToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
    }
}
