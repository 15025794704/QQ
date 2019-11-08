package com.aclass.android.qq.main;

import android.graphics.Rect;
import android.os.Bundle;

import com.aclass.android.qq.BuildConfig;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.databinding.ActivityMainBinding;

public class MainActivity extends GeneralActivity {
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
        mViews.mainViewPager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager()));
        mViews.mainViewPager.setCurrentItem(1);
    }

    @Override
    protected void consumeInsets(Rect insets) {
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
