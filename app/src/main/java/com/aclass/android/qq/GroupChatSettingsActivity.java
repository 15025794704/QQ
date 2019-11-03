package com.aclass.android.qq;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityGroupChatSettingsBinding;

import java.util.concurrent.atomic.AtomicReference;

/**
 * QQ 群聊天设置页面
 */
public class GroupChatSettingsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    // DataBinding 对象
    private ActivityGroupChatSettingsBinding mViews;
    private WindowInsets mInsets;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.csGroupToolbarMore: // more options
                return true;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBinding，摆脱 findViewById
        mViews = ActivityGroupChatSettingsBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        MyToolbar toolbar = mViews.csGroupToolbar;
        // 工具栏选项点击监听器
        toolbar.setOnMenuItemClickListener(this);

        applyInsets();
    }

    /**
     * 获得状态栏高度、导航栏高度等
     */
    private void applyInsets(){
        mViews.getRoot().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                mInsets = insets;
                consumeInsets();
                return insets;
            }
        });
    }

    /**
     * 响应窗口 insets
     */
    private void consumeInsets(){
        mViews.csGroupToolbar.setPadding(
                mInsets.getSystemWindowInsetLeft(),
                mInsets.getSystemWindowInsetTop(),
                mInsets.getSystemWindowInsetRight(),
                mInsets.getSystemWindowInsetBottom()
        );
    }
}
