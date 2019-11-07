package com.aclass.android.qq;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityMainBinding;
import com.aclass.android.qq.seek.SeekActivity;

public class MainActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 测试某个 Activity
        if (BuildConfig.DEBUG) {
            boolean canEnterTest = testMyActivity();
            if (canEnterTest) return;
        }
        // 默认的 MainActivity
        mViews = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        mViews.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mViews.mainToolbar.setOnMenuItemClickListener(this);
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.mainToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        BottomNavigationView bottomNav= mViews.mainBottomNav;
        bottomNav.setPadding(bottomNav.getPaddingStart(), bottomNav.getPaddingTop(), bottomNav.getPaddingEnd(), insets.bottom);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainToolbarMessagesMore: // more options
                startActivity(new Intent(this, SeekActivity.class));
                return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainBottomNavMessages:
                return true;
            case R.id.mainBottomNavContacts:
                return true;
            case R.id.mainBottomNavExplore:
                return true;
        }
        return true;
    }

    /**
     * 测试某个 Activity，而不修改会上传到 Github 的文件
     * 在 local.properties 文件里新增一行：testActivityName=com.aclass.android.qq.MyActivity
     * 将 MyActivity 替换为要进行测试的 Activity 名称，应用在 debug 模式启动时便会启动该 Activity
     * @return 是否能够成功进入测试
     */
    private boolean testMyActivity(){
        String testActivityName = BuildConfig.TEST_ACTIVITY_NAME;
        if (testActivityName == null || testActivityName.isEmpty()) return false;
        try {
            Class klass = Class.forName(testActivityName);
            ActivityOpreation.jumpActivity(this, klass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        finish();
        return true;
    }
}
