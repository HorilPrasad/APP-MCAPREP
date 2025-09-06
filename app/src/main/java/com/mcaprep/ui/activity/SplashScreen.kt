package com.mcaprep.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mcaprep.R
import com.mcaprep.data.local.PrefManager
import com.mcaprep.utils.NavigationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (prefManager.isLoggedIn()) {
            NavigationHelper.navigateToMain(this)
        }else {
            NavigationHelper.navigateToAuth(this)
        }
    }
}