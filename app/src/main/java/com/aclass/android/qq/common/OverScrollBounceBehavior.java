package com.aclass.android.qq.common;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * 类 iOS 边界滑动反弹效果
 * 将可滑动的 View 包裹在 {@link CoordinatorLayout CoordinatorLayout} 里面，
 * 设置该 View 的 layout_behaviour 为 OverScrollBounceBehavior
 */
public class OverScrollBounceBehavior extends CoordinatorLayout.Behavior<View> {

    private int mOverScrollY;
    private int maxOverScrollPos;
    private int maxOverScrollNeg;
    private View mTarget;
    private boolean isBouncingBack = false;

    public OverScrollBounceBehavior() {
    }

    public OverScrollBounceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        float dpMax = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, context.getResources().getDisplayMetrics());
        maxOverScrollPos = Math.round(dpMax);
        maxOverScrollNeg = -maxOverScrollPos;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        mOverScrollY = 0;
        return true;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        if (dyUnconsumed == 0 || isBouncingBack || isLimitReached()) return;
        mTarget = target;
        mOverScrollY = -dyUnconsumed;
        if (mOverScrollY > maxOverScrollPos) {
            mOverScrollY = maxOverScrollPos;
        } else if (mOverScrollY < maxOverScrollNeg) {
            mOverScrollY = maxOverScrollNeg;
        }
        final ViewGroup group = (ViewGroup) target;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = group.getChildAt(i);
            view.setTranslationY(mOverScrollY);
        }
//        if (isLimitReached()) bounceBack(mTarget);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
//        if (!isBouncingBack) bounceBack(target);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
//            if (!isBouncingBack) bounceBack(mTarget);
            return true;
        }
        return super.onTouchEvent(parent, child, ev);
    }

    private boolean isLimitReached(){
        return mOverScrollY == maxOverScrollPos || mOverScrollY == maxOverScrollNeg;
    }

    private void bounceBack(View target){
        isBouncingBack = true;
        final ViewGroup group = (ViewGroup) target;
        final int count = group.getChildCount();
        long duration = 500;
        for (int i = 0; i < count; i++) {
            final View view = group.getChildAt(i);
            ViewCompat.animate(view).translationY(0).setDuration(duration).start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mOverScrollY = 0;
                isBouncingBack = false;
            }
        }, duration);
    }
}
