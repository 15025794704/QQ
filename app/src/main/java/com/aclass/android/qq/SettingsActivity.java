package com.aclass.android.qq;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySettingsBinding;

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
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.settingsToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        LinearLayout container = mViews.settingsContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }
}
