package com.example.vahaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchIntent = packageManager.getLaunchIntentForPackage("com.example.vahahelloapp")
        launchIntent?.let { startActivity(it) }
    }
}