package com.mcaprep.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.mcaprep.R
import com.mcaprep.ui.activity.AuthActivity
import com.mcaprep.ui.activity.MainActivity
import com.mcaprep.ui.activity.ProfileActivity
import com.mcaprep.ui.activity.ResultActivity
import com.mcaprep.ui.activity.TestBuilderActivity
import com.mcaprep.ui.activity.TestScreenActivity
import com.mcaprep.ui.activity.TestSeriesActivity
import com.mcaprep.utils.Constants.COUNT
import com.mcaprep.utils.Constants.TEST_NAME
import com.mcaprep.utils.Constants.TEST_ID
import com.mcaprep.utils.Constants.TEST_TYPE

object NavigationHelper {
    private var options: ActivityOptionsCompat? = null

    // Initialize the animation once
    fun init(context: Context) {
        options = ActivityOptionsCompat.makeCustomAnimation(
            context,
            R.anim.fade_in,
            R.anim.fade_out
        )
    }

    fun navigateToMain(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToAuth(activity: Activity) {
        val intent = Intent(activity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToTestSeries(activity: Activity, testName: String, testType: String) {
        val intent = Intent(activity, TestSeriesActivity::class.java)
        intent.putExtra(TEST_NAME, testName)
        intent.putExtra(TEST_TYPE, testType)
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToTestScreen(activity: Activity, testId: String) {
        val intent = Intent(activity, TestScreenActivity::class.java)
        intent.putExtra(TEST_ID, testId)
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToResultScreen(activity: Activity, testId: String?, count: Int?) {
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra(TEST_ID, testId)
        intent.putExtra(COUNT, count)
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToProfile(activity: Activity) {
        val intent = Intent(activity, ProfileActivity::class.java)
        activity.startActivity(intent, options?.toBundle())
    }

    fun navigateToTestBuilder(activity: Activity) {
        val intent = Intent(activity, TestBuilderActivity::class.java)
        activity.startActivity(intent, options?.toBundle())
    }
}