package com.mcaprep

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import com.mcaprep.utils.NavigationHelper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@HiltAndroidApp
class MyApplication: Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        private var currentActivity: WeakReference<Activity>? = null

        fun getCurrentActivity(): Activity? {
            return currentActivity?.get()
        }

        // Updated logout to potentially use the current activity
        // Make sure this is what NavigationHelper expects or can handle.
        fun logout() {
            val activity = getCurrentActivity()
            if (activity != null) {
                CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                    Toast.makeText(activity, "Session Expired...", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    NavigationHelper.navigateToAuth(activity) // If it needs an Activity context
                }
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        NavigationHelper.init(this)
    }

    // ActivityLifecycleCallbacks methods
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
    }
    override fun onActivityPaused(activity: Activity) {
        // Clear reference if it's the activity being paused
        if (currentActivity?.get() == activity) {
            currentActivity?.clear()
        }
    }
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        // Clear reference if it's the activity being destroyed
        if (currentActivity?.get() == activity) {
            currentActivity?.clear()
        }
    }
}