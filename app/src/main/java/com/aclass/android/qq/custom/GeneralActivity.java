package com.aclass.android.qq.custom;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(Rect) consumeInsets} 方法
 */
public abstract class GeneralActivity extends AppCompatActivity {
    private Rect mWindowInsets;

    public Rect getWindowInsets(){
        return new Rect(mWindowInsets);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏 action bar，临时修补措施 // TODO: 11/10/2019 修改 App theme 并移除这几句话
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        applyEdgeToEdge();
        applyInsets();
    }

    /**
     * 实现界面能够显示在状态栏之下
     */
    private void applyEdgeToEdge(){
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(decor.getSystemUiVisibility() | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获得状态栏高度、导航栏高度等
     */
    private void applyInsets(){
        View root = getWindow().getDecorView().getRootView();
        root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                mWindowInsets = new Rect(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom()
                );
                consumeInsets(getWindowInsets());
                return insets;
            }
        });
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(Rect insets);
}
