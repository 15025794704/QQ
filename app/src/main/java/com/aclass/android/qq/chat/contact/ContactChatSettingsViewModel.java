package com.aclass.android.qq.chat.contact;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aclass.android.qq.chat.ChatSettingsViewModel;
import com.aclass.android.qq.chat.Settings;

public class ContactChatSettingsViewModel extends ViewModel implements ChatSettingsViewModel {
    MutableLiveData<ContactSettings> settings = new MutableLiveData<>();

    @Override
    public MutableLiveData<? extends Settings> getSettings() {
        return settings;
    }
}
