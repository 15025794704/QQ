package com.aclass.android.qq.main;

import android.app.Application;

public class MainApplication extends Application {
    public static MainApplication INSTANCE;

    private final int timeNew = 3000;
    /**
     * application 创建时间
     */
    private long timeCreate;

    @Override
    public void onCreate() {
        timeCreate = System.currentTimeMillis();
        super.onCreate();
        INSTANCE = this;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    /**
     * 检测是否 application 创建时间与当前时间相比很短（冷启动）
     * @return application 创建时间是否小于 {@link MainApplication#timeNew timeNew}
     */
    public boolean isNew(){
        return System.currentTimeMillis() - timeCreate < timeNew;
    }
}
