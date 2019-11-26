package com.aclass.android.qq.chat.group;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.aclass.android.qq.chat.ChatSettingsViewModel;
import com.aclass.android.qq.chat.Settings;

public class GroupChatSettingsViewModel extends ViewModel implements ChatSettingsViewModel {
    MutableLiveData<GroupSettings> settings = new MutableLiveData<>();
    MutableLiveData<Bitmap> groupProfilePhoto = new MutableLiveData<>();

    @Override
    public MutableLiveData<? extends Settings> getSettings() {
        return settings;
    }
}
