package me.zhenhao.forcedroid.fuzzdroid.crashreporter

import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.ServerCommunicator
import me.zhenhao.forcedroid.fuzzdroid.tracing.BytecodeLogger
import java.util.*


/**
 * Created by tom on 6/8/17.
 */
object CrashReporter {

    private val uch = object : Thread.UncaughtExceptionHandler {

        private val communicator = ServerCommunicator(this)

        override fun uncaughtException(arg0: Thread, arg1: Throwable) {
            Log.i(SharedClassesSettings.TAG, "Crash reporter started: " + arg1.toString()
                    + " at " + arg1.stackTrace)
            if (arg1.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause: " + arg1.cause.toString())
            if (arg1.cause!!.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause 2: " + arg1!!.cause!!.cause.toString())
            if (arg1.cause!!.cause!!.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause 3: " + arg1!!.cause!!.cause!!.cause.toString())

            // Make sure that we flush the trace items before we die
            BytecodeLogger.dumpTracingDataSynchronous()

            // Send the crash report
            val ci = CrashReportItem(arg1.message!!,
                    BytecodeLogger.getLastExecutedStatement())
            communicator.send(Collections.singleton(ci), true)
        }

    }

    fun registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(uch)
    }

}