package com.aclass.android.qq.seek;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityNewContactBinding;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 添加好友，填写验证消息
 */
public class NewContactActivity extends GeneralActivity implements View.OnClickListener {
    public static String ARG_CONTACT = "Contact";

    private ActivityNewContactBinding mViews;

    private AtomicReference<User> mContactRef = new AtomicReference<>();

    private User getContact(){
        return mContactRef.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityNewContactBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                mContactRef.set((User) intent.getParcelableExtra(ARG_CONTACT));
                final User contact = getContact();
                final Bitmap profilePhoto = ProfileUtil.getRoundProfilePhoto(NewContactActivity.this, contact.getQQNum(), null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.newContactProfilePhoto.setImageBitmap(profilePhoto);
                        mViews.newContactName.setText(contact.getNiCheng());
                        mViews.newContactInfo.setText(contact.getSex() + " " + contact.getAge() + "岁 " + contact.getAddress());
                    }
                });
            }
        }).start();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar tb = mViews.newContactToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        switch (v.getId()){
            case R.id.newContactToolbarCancel:
                finish();
                break;
            case R.id.newContactToolbarSend:
                String groupTag = getText(mViews.newContactGroupTag);
                if (groupTag.isEmpty()) {
                    Toast.makeText(NewContactActivity.this, "分组名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendRequest();
                break;
        }
    }

    private void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String remark = getText(mViews.newContactRemark);
                String groupTag = getText(mViews.newContactGroupTag);
                Friend friend = new Friend();
                friend.setQQ1(Attribute.currentAccount.getQQNum());
                friend.setQQ2(getContact().getQQNum());
                friend.setIsAgree(0);
                friend.setBeiZhu(remark.isEmpty() ? null : remark);
                friend.setQQgroup(groupTag.isEmpty() ? null : groupTag);
                MyDateBase database = new MyDateBase();
                int result = database.insertEntity(friend);
                database.Destory();
            }
        }).start();
        finish();
    }

    private String getText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null) return "";
        return editable.toString();
    }
}
