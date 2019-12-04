package com.aclass.android.qq.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ProfileUtil;
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
        User currentAccount = Attribute.currentAccount;
        Bitmap bitmap = ProfileUtil.getRoundProfilePhoto(this, null, null);
        mViews.settingsAccountsProfilePhoto.setImageBitmap(bitmap);
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
                quit();
                break;
        }
    }

    private void logOut(Context context){
        context.getSharedPreferences("GeneralPrefs", MODE_PRIVATE).edit().putBoolean("isLogin", false).apply();
        Attribute.isAccountInitialized = false;
        setResult(RESULT_OK);
        finish();
    }

    private void quit(){
    }
}
