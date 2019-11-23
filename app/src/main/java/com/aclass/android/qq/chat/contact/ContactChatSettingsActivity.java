package com.aclass.android.qq.chat.contact;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.ThemeUtil;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityContactChatSettingsBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友聊天设置页面
 */
public class ContactChatSettingsActivity extends GeneralActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static String ARG_NUM = "contactNum";

    private ActivityContactChatSettingsBinding mViews;
    private ContactChatSettingsViewModel mViewModel;
    private String contactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityContactChatSettingsBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        final Context context = this;

        Intent intent = getIntent();
        contactNum = intent.getStringExtra(ARG_NUM);

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
                contactNum = contactSettings.contactNum;
                bindData(context, contactSettings);
            }
        });
        if (mViewModel.contactSettings.getValue() == null){
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
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar tb = mViews.chatSettingsContactToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
        LinearLayout container = mViews.chatSettingsContactContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    private void bindData(Context context, final ContactSettings contactSettings){
        Bitmap bitmap = Attribute.userHeadList.get(contactSettings.contactNum);
        if (bitmap == null) return;
        int colorOption = ThemeUtil.getColor(context, R.attr.mColorOptionGo);
        Drawable[] drawables = mViews.chatSettingsContactInfo.getCompoundDrawablesRelative();
        drawables[2].setTint(colorOption);
        int length = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.getResources().getDisplayMetrics()));
        Drawable profilePhoto = new BitmapDrawable(getResources(), GraphicsUtil.round(bitmap));
        profilePhoto.setBounds(0, 0, length, length);
        mViews.chatSettingsContactInfo.setCompoundDrawablesRelative(profilePhoto, null, drawables[2], null);
        mViews.chatSettingsContactInfo.setText(contactSettings.contactName);

        Switch[] switches = new Switch[]{
                mViews.chatSettingsContactPinnedTop,
                mViews.chatSettingsContactDND,
                mViews.chatSettingsContactHidden,
                mViews.chatSettingsContactBlocked
        };
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(null);
        mViews.chatSettingsContactPinnedTop.setChecked(contactSettings.isPinnedTop);
        mViews.chatSettingsContactDND.setChecked(contactSettings.isDND);
        mViews.chatSettingsContactHidden.setChecked(contactSettings.isBlocked); // todo
        mViews.chatSettingsContactBlocked.setChecked(contactSettings.isBlocked);
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        Context context = this;
        switch (v.getId()){
            case R.id.chatSettingsContactInfo:
                contactInfo(context);
                break;
            case R.id.chatSettingsContactGroupChat:
                newGroupChat();
                break;
            case R.id.chatSettingsContactRemoveContact:
                removeContact();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == null) return;
        switch (buttonView.getId()){
            case R.id.chatSettingsContactPinnedTop:
                changePinnedTop(isChecked);
                break;
            case R.id.chatSettingsContactDND:
                changeDND(isChecked);
                break;
            case R.id.chatSettingsContactHidden:
                changeHidden(isChecked);
                break;
            case R.id.chatSettingsContactBlocked:
                changeBlocked(isChecked);
                break;
        }
    }

    private void contactInfo(Context context) {
        Intent intent = new Intent(context, MyDataActivity.class);
        intent.putExtra("qqNum", contactNum);
        startActivity(intent);
    }

    private void newGroupChat(){
    }

    private void removeContact(){
    }

    private void changePinnedTop(boolean newValue){
        ContactSettings settings = mViewModel.contactSettings.getValue();
        if (settings == null) return;
        settings.isPinnedTop = newValue;
        updateData();
    }

    private void changeDND(boolean newValue){
        ContactSettings settings = mViewModel.contactSettings.getValue();
        if (settings == null) return;
        settings.isDND = newValue;
        updateData();
    }

    private void changeHidden(boolean newValue){
        ContactSettings settings = mViewModel.contactSettings.getValue();
        if (settings == null) return;
        settings.isBlocked = newValue; // todo
        updateData();
    }

    private void changeBlocked(boolean newValue){
        ContactSettings settings = mViewModel.contactSettings.getValue();
        if (settings == null) return;
        settings.isBlocked = newValue;
        updateData();
    }

    private void updateData(){
        final ContactSettings settings = mViewModel.contactSettings.getValue();
        if (settings == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                int result = dateBase.updateEntity(settings.toFriend());
                dateBase.Destory();
            }
        }).start();
    }
}
