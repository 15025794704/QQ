package com.aclass.android.qq;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.databinding.ActivityWindowMessageBinding;

public class MessageWindowActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    // DataBinding 对象
    private ActivityWindowMessageBinding mViews;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.messageToolbarCall: // 语音通话
                ActivityOpreation.jumpActivity(this,VideoWindowActivity.class);
                return true;
            case R.id.messageToolbarInfo: // 聊天详情
                return true;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_message);
        init();
    }

    protected void init(){
        // DataBinding，摆脱 findViewById
        mViews = ActivityWindowMessageBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        Toolbar toolbar = mViews.messageToolbar;
        // 工具栏选项
        toolbar.inflateMenu(R.menu.toolbar_message);
        // 工具栏选项点击监听器
        toolbar.setOnMenuItemClickListener(this);
        // 设置工具栏导航图标
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24);

        toolbar.getNavigationIcon().setTint(Color.WHITE);
        for(int i=0;i<toolbar.getMenu().size();i++)
            toolbar.getMenu().getItem(i).getIcon().setTint(Color.WHITE);
        // 设置工具栏导航按键事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置工具栏标题文字
        mViews.messageToolbarTitle.setText("吴小吴");
    }
}
