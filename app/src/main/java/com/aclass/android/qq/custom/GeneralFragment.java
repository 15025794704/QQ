package com.aclass.android.qq.custom;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(Rect) consumeInsets} 方法
 */
public abstract class GeneralFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (!(activity instanceof GeneralActivity)) return;
        GeneralViewModel generalViewModel = ViewModelProviders.of(activity).get(GeneralViewModel.class);
        generalViewModel.windowInsets.observe(this, new Observer<Rect>() {
            @Override
            public void onChanged(@Nullable Rect rect) {
                if (rect == null) return;
                consumeInsets(rect);
            }
        });
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(Rect insets);
}
