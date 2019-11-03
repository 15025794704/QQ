package com.aclass.android.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aclass.android.qq.databinding.ActivityWindowSettingBinding;

public class SettingWindowActivity extends AppCompatActivity {
    // DataBinding 对象
    private ActivityWindowSettingBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBinding，摆脱 findViewById
        mViews = ActivityWindowSettingBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        Toolbar toolbar = mViews.settingsToolbar;
        // 设置工具栏导航图标
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24);
    }
}
