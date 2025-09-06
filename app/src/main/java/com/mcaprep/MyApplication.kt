package com.mcaprep

import android.app.Application
import com.mcaprep.utils.NavigationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NavigationHelper.init(this)
    }
}