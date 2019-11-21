package com.aclass.android.qq.settings;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        Context context = this;
        switch (v.getId()){
            case R.id.settingsAccountsLogOut:
                logOut(context);
                break;
            case R.id.settingsAccountsQuit:
                break;
        }
    }

    private void logOut(Context context){
        getSharedPreferences("GeneralPrefs", MODE_PRIVATE).edit().putBoolean("isLogin", false).apply();
        Toast.makeText(context, "You are logged out", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
