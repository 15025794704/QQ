package com.aclass.android.qq.custom;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 * 实现状态栏透明
 * 使用方法：
 * 1. 继承这个类
 * 2. 在 {@link AppCompatActivity#onCreate(Bundle) onCreate} 里的 {@link AppCompatActivity#setContentView(View) setContentView} 之后调用 {@link #applyInsets() applyInsets}
 * 3. 实现 {@link #consumeInsets(Rect) consumeInsets}
 */
public abstract class GeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyEdgeToEdge();
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
     * 在 {@link AppCompatActivity#setContentView(View) setContentView} 之后调用
     */
    protected void applyInsets(){
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
