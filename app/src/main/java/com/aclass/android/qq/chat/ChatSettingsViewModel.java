package com.aclass.android.qq.chat;

import android.arch.lifecycle.MutableLiveData;

public interface ChatSettingsViewModel {
    MutableLiveData<? extends Settings> getSettings();
}
