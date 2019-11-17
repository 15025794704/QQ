package com.aclass.android.qq.custom;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Rect;

public class GeneralViewModel extends ViewModel {
    public MutableLiveData<Rect> windowInsets = new MutableLiveData<>();
}
