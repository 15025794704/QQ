package com.aclass.android.qq.chat.contact;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.chat.ChatSettingsActivity;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityContactChatSettingsBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友聊天设置页面
 */
public class ContactChatSettingsActivity extends ChatSettingsActivity {

    public static String ARG_NUM = "contactNum";

    private ActivityContactChatSettingsBinding mViews;
    private ContactChatSettingsViewModel mViewModel;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityContactChatSettingsBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        final Context context = this;

        Intent intent = getIntent();
        number = intent.getStringExtra(ARG_NUM);
        if (number == null) {
            finish();
            return;
        }

        MyToolbar toolbar = mViews.chatSettingsContactToolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = ViewModelProviders.of(this).get(ContactChatSettingsViewModel.class);
        chatSettingsViewModel = mViewModel;
        mViewModel.settings.observe(this, new Observer<ContactSettings>() {
            @Override
            public void onChanged(@Nullable ContactSettings contactSettings) {
                if (contactSettings == null) return;
                number = contactSettings.number;
                bindData(context, contactSettings);
            }
        });
        if (mViewModel.settings.getValue() == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ContactSettings contactSettings = ContactSettings.get(number, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewModel.settings.setValue(contactSettings);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void consumeInsets(Rect insets) {
        consumeInsets(insets, mViews.chatSettingsContactToolbar, mViews.chatSettingsContactContainer);
    }

    private void bindData(final Context context, final ContactSettings settings){
        Bitmap profilePhoto = Attribute.userHeadList.get(settings.number);
        if (profilePhoto == null) profilePhoto = ProfileUtil.getDefaultProfilePhoto(context);
        bindDataProfilePhoto(context, mViews.chatSettingsContactInfo, profilePhoto);
        mViews.chatSettingsContactInfo.setText(settings.name);
        mViews.chatSettingsContactRemark.setText(settings.remark);
        mViews.chatSettingsContactGroupTag.setText(settings.groupTag);
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    final String newValue = v.getText().toString();
                    switch (v.getId()) {
                        case R.id.chatSettingsContactRemark:
                            changeRemark(newValue);
                            break;
                        case R.id.chatSettingsContactGroupTag:
                            if (newValue.isEmpty()) {
                                Toast.makeText(context, "分组名称不能为空", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            changeGroupTag(newValue);
                            break;
                    }
                    return true;
                }
                return false;
            }
        };
        mViews.chatSettingsContactRemark.setOnEditorActionListener(editorActionListener);
        mViews.chatSettingsContactGroupTag.setOnEditorActionListener(editorActionListener);

        Switch[] switches = new Switch[]{
                mViews.chatSettingsContactPinnedTop,
                mViews.chatSettingsContactDND,
                mViews.chatSettingsContactHidden,
                mViews.chatSettingsContactBlocked
        };
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(null);
        mViews.chatSettingsContactPinnedTop.setChecked(settings.isPinnedTop);
        mViews.chatSettingsContactDND.setChecked(settings.isDND);
        mViews.chatSettingsContactHidden.setChecked(settings.isHidden);
        mViews.chatSettingsContactBlocked.setChecked(settings.isBlocked);
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
        intent.putExtra("qqNum", number);
        startActivity(intent);
    }

    private void newGroupChat(){
    }

    private void removeContact(){
    }

    private void changeGroupTag(String newValue){
        ContactSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        if (settings.groupTag.equals(newValue)) return;
        settings.groupTag = newValue;
        updateData();
    }

    private void changeBlocked(boolean newValue){
        ContactSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        if (settings.isBlocked == newValue) return;
        settings.isBlocked = newValue;
        updateData();
    }

    @Override
    protected void updateData(){
        final ContactSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase database = new MyDateBase();
                int result = database.updateEntity(settings.toFriend());
                database.Destory();
            }
        }).start();
    }
}
