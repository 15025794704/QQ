package com.aclass.android.qq.seek;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityNewFriendBinding;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 添加好友，填写验证消息
 */
public class NewFriendActivity extends GeneralActivity {
    public static String ARG_CONTACT = "Contact";

    private ActivityNewFriendBinding mViews;

    private AtomicReference<User> mContactRef = new AtomicReference<>();

    private User getContact(){
        return mContactRef.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityNewFriendBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                mContactRef.set((User) intent.getParcelableExtra(ARG_CONTACT));
                MyDateBase dateBase = new MyDateBase();
                final User contact = getContact();
                final Bitmap profilePhoto = dateBase.getImageByQQ(contact.getQQNum());
                dateBase.Destory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.newFriendProfilePhoto.setImageBitmap(profilePhoto);
                        mViews.newFriendName.setText(contact.getNiCheng());
                        mViews.newFriendInfo.setText(contact.getSex() + " " + contact.getAge() + "岁 " + contact.getAddress());
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
                        friend.setQQ1(Attribute.currentAccount.getQQNum());
                        friend.setQQ2(getContact().getQQNum());
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
