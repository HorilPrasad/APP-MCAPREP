package com.mcaprep.utils

import android.annotation.SuppressLint
import java.util.TimeZone
import android.os.CountDownTimer
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun formatUtcToIst(input: String): String {
    val result =
    try {// 1️⃣ Parse the ISO date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC") // It's in UTC
        }
        val date = inputFormat.parse(input)

        // 2️⃣ Format to desired output
        val outputFormat = SimpleDateFormat("dd MMM, yy hh:mm a", Locale.US).apply {
            timeZone = TimeZone.getDefault() // or any desired TimeZone
        }
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")


        outputFormat.format(date)
    } catch (e: Exception) {
        e.localizedMessage
    }
    return result
}
fun convertSecondsToMinutes(seconds: Int): Int {
    val minutes = seconds / 60
    return minutes
}

@SuppressLint("DefaultLocale")
fun formatSeconds(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

fun startTimer(seconds: Int, textView: TextView, onFinish: () -> Unit) {
    val timer = object : CountDownTimer(seconds * 1000L, 1000) { // Convert seconds to milliseconds
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000
            textView.text = formatSeconds(secondsRemaining)
        }

        override fun onFinish() {
            onFinish() // Trigger the callback when the timer finishes
        }
    }
    timer.start()
}
