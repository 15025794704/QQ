package com.aclass.android.qq;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySettingsAccountsBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;

public class SettingsAccountsActivity extends GeneralActivity {
    private ActivitySettingsAccountsBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivitySettingsAccountsBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        mViews.settingsAccountsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final User currentAccount = Attribute.currentAccount;
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.settingsAccountsToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
    }
}
