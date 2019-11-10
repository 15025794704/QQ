package com.aclass.android.qq;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySettingsBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

public class SettingsActivity extends GeneralActivity {
    // DataBinding 对象
    private ActivitySettingsBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mViews.settingsAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SettingsAccountsActivity.class));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                final User myAccount = dateBase.getUser("1234567890");
                if (myAccount == null) return;
                Attribute.currentAccount = myAccount;
                final User currentAccount = Attribute.currentAccount;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViews.settingsPhone.setText(currentAccount.getPhone());
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
}
