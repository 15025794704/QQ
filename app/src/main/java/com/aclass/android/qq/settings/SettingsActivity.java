package com.aclass.android.qq.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySettingsBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;

public class SettingsActivity extends GeneralActivity implements View.OnClickListener {
    private static int REQUEST_ACCOUNTS = 1;
    // DataBinding 对象
    private ActivitySettingsBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        // DataBinding，摆脱 findViewById
        mViews = ActivitySettingsBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        MyToolbar toolbar = mViews.settingsToolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User currentAccount = Attribute.currentAccount;
                final Bitmap bitmap = ProfileUtil.getRoundProfilePhoto(context, null, null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.settingsPhone.setText(currentAccount.getPhone());
                        mViews.settingsProfilePhoto.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.settingsToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        LinearLayout container = mViews.settingsContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 退出账号
        if (requestCode == REQUEST_ACCOUNTS && resultCode == RESULT_OK){
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        Context context = this;
        switch (v.getId()){
            case R.id.settingsAccounts:
                startActivityForResult(new Intent(context, SettingsAccountsActivity.class), REQUEST_ACCOUNTS);
                break;
        }
    }
}
