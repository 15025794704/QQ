package com.aclass.android.qq.custom;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(Rect) consumeInsets} 方法
 */
public abstract class GeneralActivity extends AppCompatActivity {
    private boolean isInsetsApplied = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyEdgeToEdge();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toApplyInsets();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        toApplyInsets();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        toApplyInsets();
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
        toApplyInsets();
    }

    private void toApplyInsets(){
        if (isInsetsApplied) return;
        isInsetsApplied = true;
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
        getWindow().getDecorView().getRootView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                consumeInsets(new Rect(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom()
                ));
                return insets;
            }
        });
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(Rect insets);
}
