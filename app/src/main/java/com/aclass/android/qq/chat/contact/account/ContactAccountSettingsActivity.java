package com.aclass.android.qq.chat.contact.account;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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

import com.aclass.android.qq.R;
import com.aclass.android.qq.chat.ChatSettingsActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityContactAccountSettingsBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * QQ 好友设置页面
 */
public class ContactAccountSettingsActivity extends ChatSettingsActivity {

    public static String ARG_NUM = "contactNum";

    private ActivityContactAccountSettingsBinding mViews;
    private ContactAccountSettingsViewModel mViewModel;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivityContactAccountSettingsBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        final Context context = this;

        Intent intent = getIntent();
        number = intent.getStringExtra(ARG_NUM);
        if (number == null) {
            finish();
            return;
        }

        MyToolbar toolbar = mViews.accountSettingsContactToolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = ViewModelProviders.of(this).get(ContactAccountSettingsViewModel.class);
        chatSettingsViewModel = mViewModel;
        mViewModel.settings.observe(this, new Observer<ContactAccountSettings>() {
            @Override
            public void onChanged(@Nullable ContactAccountSettings accountSettings) {
                if (accountSettings == null) return;
                number = accountSettings.number;
                bindData(context, accountSettings);
            }
        });
        if (mViewModel.settings.getValue() == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ContactAccountSettings accountSettings = ContactAccountSettings.get(number, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewModel.settings.setValue(accountSettings);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void consumeInsets(Rect insets) {
        consumeInsets(insets, mViews.accountSettingsContactToolbar, mViews.accountSettingsContactContainer);
    }

    private void bindData(final Context context, final ContactAccountSettings settings){
        mViews.accountSettingsContactRemark.setText(settings.remark);
        mViews.accountSettingsContactGroupTag.setText(settings.groupTag);
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    final String newValue = v.getText().toString();
                    switch (v.getId()) {
                        case R.id.accountSettingsContactRemark:
                            changeRemark(newValue);
                            Attribute.agreeFriendClick = 1;
                            break;
                        case R.id.accountSettingsContactGroupTag:
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
        mViews.accountSettingsContactRemark.setOnEditorActionListener(editorActionListener);
        mViews.accountSettingsContactGroupTag.setOnEditorActionListener(editorActionListener);

        Switch[] switches = new Switch[]{
                mViews.accountSettingsContactDND,
                mViews.accountSettingsContactBlocked
        };
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(null);
        mViews.accountSettingsContactDND.setChecked(settings.isDND);
        mViews.accountSettingsContactBlocked.setChecked(settings.isBlocked);
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        Context context = this;
        switch (v.getId()){
            case R.id.accountSettingsContactRemoveContact:
                removeContact();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == null) return;
        switch (buttonView.getId()){
            case R.id.accountSettingsContactDND:
                changeDND(isChecked);
                break;
            case R.id.accountSettingsContactBlocked:
                changeBlocked(isChecked);
                break;
        }
    }

    private void removeContact(){
    }

    private void changeGroupTag(String newValue){
        ContactAccountSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        if (settings.groupTag.equals(newValue)) return;
        settings.groupTag = newValue;
        Attribute.agreeFriendClick = 1;
        updateData();
    }

    private void changeBlocked(boolean newValue){
        ContactAccountSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        if (settings.isBlocked == newValue) return;
        settings.isBlocked = newValue;
        updateData();
    }

    @Override
    protected void updateData(){
        final ContactAccountSettings settings = mViewModel.settings.getValue();
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
