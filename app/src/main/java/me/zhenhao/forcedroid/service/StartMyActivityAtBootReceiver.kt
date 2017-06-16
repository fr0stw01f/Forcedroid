package me.zhenhao.forcedroid.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.zhenhao.forcedroid.view.SettingsActivity


/**
 * Created by tom on 6/16/17.
 */
class StartMyActivityAtBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, SettingsActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }
}