package me.zhenhao.forcedroid.fuzzdroid.tracing

import android.os.Environment
import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import java.io.File


/**
 * Created by tom on 6/9/17.
 */
object FileBasedTracingUtils {
    fun getFuzzerDirectory(): File {
        val storageDir = File(Environment.getExternalStorageDirectory(), "evoFuzzDumps/")
        if (!storageDir.exists() && !storageDir.mkdirs())
            Log.e(SharedClassesSettings.TAG, "Could not create communication directory for watchdog: " + storageDir)
        storageDir.setWritable(true, false)
        Log.i(SharedClassesSettings.TAG, "Communication directory for watchdog: " + storageDir)
        return storageDir
    }

}