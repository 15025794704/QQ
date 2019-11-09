package com.aclass.android.qq.custom;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(Rect) consumeInsets} 方法
 */
public abstract class GeneralFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (!(activity instanceof GeneralActivity)) return;
        consumeInsets(((GeneralActivity) activity).getWindowInsets());
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(Rect insets);
}
