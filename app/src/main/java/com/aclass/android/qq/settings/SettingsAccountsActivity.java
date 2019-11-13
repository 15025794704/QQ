package com.aclass.android.qq.settings;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySettingsAccountsBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;

public class SettingsAccountsActivity extends GeneralActivity implements View.OnClickListener {
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
        mViews.settingsAccountsProfilePhoto.setImageBitmap(Attribute.currentAccountProfilePhoto);
        mViews.settingsAccountsName.setText(currentAccount.getNiCheng());
        mViews.settingsAccountsInfo.setText(currentAccount.getQQNum());
        mViews.settingsAccountsLogOut.setOnClickListener(this);
        mViews.settingsAccountsQuit.setOnClickListener(this);
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.settingsAccountsToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        LinearLayout container = mViews.settingsAccountsContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        switch (v.getId()){
            case R.id.settingsAccountsLogOut:
                break;
            case R.id.settingsAccountsQuit:
                break;
        }
    }
}
