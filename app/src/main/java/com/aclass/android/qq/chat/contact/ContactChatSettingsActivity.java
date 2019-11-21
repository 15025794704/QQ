package com.aclass.android.qq.chat.contact;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.BuildConfig;
import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityContactChatSettingsBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友聊天设置页面
 */
public class ContactChatSettingsActivity extends GeneralActivity implements View.OnClickListener {

    public static String ARG_NUM = "contactNum";

    private ActivityContactChatSettingsBinding mViews;
    private ContactChatSettingsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityContactChatSettingsBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());

        Intent intent = getIntent();
        final String contactNum = BuildConfig.DEBUG ? "1234567890" : intent.getStringExtra(ARG_NUM);

        MyToolbar toolbar = mViews.chatSettingsContactToolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = ViewModelProviders.of(this).get(ContactChatSettingsViewModel.class);
        mViewModel.contactSettings.observe(this, new Observer<ContactSettings>() {
            @Override
            public void onChanged(@Nullable ContactSettings contactSettings) {
                if (contactSettings == null) return;
                bindData(contactSettings);
            }
        });
        mViewModel.friendProfilePhoto.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                if (bitmap == null) return;
                mViews.chatSettingsContactInfo.setCompoundDrawables(new BitmapDrawable(getResources(), bitmap), null, null, null);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ContactSettings contactSettings = ContactSettings.get(contactNum, null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.contactSettings.setValue(contactSettings);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar tb = mViews.chatSettingsContactToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
        LinearLayout container = mViews.chatSettingsContactContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    private void bindData(final ContactSettings contactSettings){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                final Bitmap profilePhoto = dateBase.getImageByQQ(contactSettings.contactNum);
                dateBase.Destory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.friendProfilePhoto.setValue(profilePhoto);
                    }
                });
            }
        }).start();
        mViews.chatSettingsContactInfo.setText(contactSettings.contactName);
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        Context context = this;
        switch (v.getId()){
            case R.id.chatSettingsContactInfo:
                contactInfo(context);
                break;
            case R.id.chatSettingsContactRemoveContact:
                removeContact();
                break;
        }
    }

    private void contactInfo(Context context) {
        Intent intent = new Intent(context, MyDataActivity.class);
        intent.putExtra("qqNum", Attribute.currentAccount.getQQNum());
        startActivity(intent);
    }

    private void removeContact(){
    }
}
