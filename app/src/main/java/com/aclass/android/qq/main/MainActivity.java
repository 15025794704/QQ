package com.aclass.android.qq.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import com.aclass.android.qq.BuildConfig;
import com.aclass.android.qq.LoginWindowActivity;
import com.aclass.android.qq.SplashActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.databinding.ActivityMainBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.contacts.CondactMain;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * 应用主界面
 * 主要是一个 {@link android.support.v4.view.ViewPager ViewPager}，含两个 fragment
 * 1. {@link MainFragment MainFragment}: 应用最主要的界面，包含聊天列表等
 * 2. {@link DrawerFragment DrawerFragment}: 应用从侧面滑动出来的页面
 */
public class MainActivity extends GeneralActivity {
    private static int REQUEST_SPLASH = 1;

    private SharedPreferences prefGeneral;
    private boolean isScreenInitialized = false;

    private ActivityMainBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 应用是否是冷启动
        boolean isColdStart = MainApplication.INSTANCE.isNew();
        super.onCreate(savedInstanceState);

        // 默认的 MainActivity
        prefGeneral = getSharedPreferences("GeneralPrefs", MODE_PRIVATE);
        if (isColdStart) {
            startActivityForResult(new Intent(this, SplashActivity.class), REQUEST_SPLASH);// SplashActivity.class
            return;
        }

        init();
    }

    /**
     * 本 activity 没有 UI，对窗口 insets 的响应应当在对应的 fragment 里面进行
     */
    @Override
    protected void consumeInsets(Rect insets) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isScreenInitialized) return;
        checkAccount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SPLASH){
            if (resultCode == RESULT_OK){
                finish();
                return;
            }
            init();
        }
    }

    private void init(){
        if (!checkAccount()) return;
        Attribute.QQ = prefGeneral.getString("loginUserName", "");
        initAccount();

        // 测试某个 Activity
        if (BuildConfig.DEBUG) {
            boolean canEnterTest = testMyActivity();
            if (canEnterTest) {
                isScreenInitialized = true;
                return;
            }
        }

        initScreen();
        isScreenInitialized = true;
    }

    private boolean checkAccount(){
        boolean isLoggedIn = prefGeneral.getBoolean("isLogin", false);
        if (!isLoggedIn){
            startActivity(new Intent(this, LoginWindowActivity.class));
            finish();
        }
        return isLoggedIn;
    }

    private void initAccount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                final User myAccount = dateBase.getUser(Attribute.QQ);
                if (myAccount == null) return;
                final Bitmap profilePhoto = dateBase.getImageByQQ(myAccount.getQQNum());
                Attribute.currentAccount = myAccount;
                Attribute.currentAccountProfilePhoto = profilePhoto;
                Attribute.isAccountInitialized = true;
            }
        }).start();
    }

    private void initScreen(){
        mViews = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());
        // 通过左右滑动在主页面和抽屉页面之间切换
        mViews.mainViewPager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager()));
        // 将主页面设置为默认进入的页面
        setPagerItem(1);
    }

    /**
     * 供 fragment 调用以切换页面
     */
    void setPagerItem(int item){
        mViews.mainViewPager.setCurrentItem(item);
    }

    @Override
    public void onBackPressed() {
        // 在抽屉页面时，返回到主界面
        if (mViews.mainViewPager.getCurrentItem() == 0){
            setPagerItem(1);
            return;
        }
        super.onBackPressed();
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
