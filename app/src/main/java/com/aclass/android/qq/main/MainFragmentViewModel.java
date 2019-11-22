package com.aclass.android.qq.main;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;

public class MainFragmentViewModel extends ViewModel {
    @IdRes int itemId = 0;
    Bitmap accountProfilePhoto;
}
