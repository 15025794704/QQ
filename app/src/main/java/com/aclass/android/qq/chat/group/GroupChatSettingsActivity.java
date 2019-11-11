package com.aclass.android.qq.chat.group;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityGroupChatSettingsBinding;

/**
 * QQ 群聊天设置页面
 */
public class GroupChatSettingsActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener {
    // DataBinding 对象
    private ActivityGroupChatSettingsBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBinding，摆脱 findViewById
        mViews = ActivityGroupChatSettingsBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());

        MyToolbar toolbar = mViews.chatSettingsGroupToolbar;
        // 工具栏选项点击监听器
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void consumeInsets(Rect insets) {
        Toolbar tb = mViews.chatSettingsGroupToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatSettingsGroupToolbar: // more options
                return true;
        }
        return true;
    }
}