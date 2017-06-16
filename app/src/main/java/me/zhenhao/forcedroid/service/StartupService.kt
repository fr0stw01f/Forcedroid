package me.zhenhao.forcedroid.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.view.SettingsActivity.Companion.context

/**
 * Created by tom on 6/16/17.
 */
class StartupService : IntentService("Service") {

    override fun onCreate() {
        super.onCreate()
        Log.d(SharedClassesSettings.TAG, "<Context> $context")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(SharedClassesSettings.TAG, "<Intent> $intent")
    }

}