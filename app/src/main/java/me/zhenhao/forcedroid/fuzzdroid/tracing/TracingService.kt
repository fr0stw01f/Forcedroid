package me.zhenhao.forcedroid.fuzzdroid.tracing

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import me.zhenhao.forcedroid.fuzzdroid.SharedClassesSettings
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.IClientRequest
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.ServerCommunicator
import java.util.concurrent.LinkedBlockingQueue


/**
 * Created by tom on 6/8/17.
 */
class TracingService : Service() {

    init {
        Log.i(SharedClassesSettings.TAG_TS, "TracingService created.");
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(SharedClassesSettings.TAG_TS, "TracingService action started.")

        // If we have not started our communication with the remote server yet,
        // we do this now
        if (!DUMPER_STARTED) {
            DUMPER_STARTED = true
            startDumper()
        }

        // Process the current command
        if (intent.action == ACTION_ENQUEUE_TRACE_ITEM
                && intent.hasExtra(EXTRA_ITEM_TYPE)
                && intent.hasExtra(EXTRA_TRACE_ITEM)) {
            val itemType = intent.getStringExtra(EXTRA_ITEM_TYPE)
            when (itemType) {
                ITEM_TYPE_PATH_TRACKING -> {
                    val ti: TraceItem = intent.getParcelableExtra(EXTRA_TRACE_ITEM)
                    enqueueTraceItem(ti)
                }
            }
        } else if (intent.action == ACTION_DUMP_QUEUE)
            Thread(Runnable {
                Log.i(SharedClassesSettings.TAG, "Flushing queue, explicit request...")
                dumpQueue()
            }).start()
        else if (intent.action == ACTION_DUMP_QUEUE_SYNCHRONOUS) {
            Log.i(SharedClassesSettings.TAG_TS, "Flushing queue (blocking mode), " + "explicit request...")
            dumpQueue(true)
        } else if (intent.action == ACTION_NULL) {
            // Do nothing. This action is mainly used for later binding to the
            // service.
            Log.i(SharedClassesSettings.TAG_TS, "Starting TracingService...")
        } else
            Log.e(SharedClassesSettings.TAG_TS, String.format("Invalid action: %s",
                    intent.action))

        Log.i(SharedClassesSettings.TAG_TS, "TracingService action done.")
        return Service.START_STICKY
    }

    internal inner class HandlerDumpQueue(looper: Looper): Handler(looper) {

        private val dumpRunnable = Runnable {
            // If the trace queue is longer than the limit, we dump it
            if (traceQueue.size > QUEUE_DUMP_LIMIT) {
                Log.i(SharedClassesSettings.TAG_TS, "Flushing queue, size limit exceeded...")
                dumpQueue()
            }
            else
                Log.i(SharedClassesSettings.TAG_TS, "Tracing queue is at " + traceQueue.size + " elements")
            //postDelayed(dumpRunnable, DUMP_TIMEOUT)
        }

        init {
            postDelayed(dumpRunnable, DUMP_TIMEOUT)
        }
    }

    private fun startDumper() {
        val thread = HandlerThread("ServiceDumpTraceQueue", THREAD_PRIORITY_BACKGROUND)
        thread.start()
        handlerDumpQueue = HandlerDumpQueue(thread.looper)
    }

    private fun dumpQueue(waitForReturn: Boolean = false) {
        Log.i(SharedClassesSettings.TAG_TS, "Flushing the queue of " + traceQueue.size
                + " items in thread " + Thread.currentThread().id)

        // Dump the current contents of the queue
        val items = ArrayList<IClientRequest>(traceQueue.size)
        while (!traceQueue.isEmpty()) {
            val ti = traceQueue.poll() ?: break
            items.add(ti)
        }
        communicator.send(items, waitForReturn)
    }

    internal inner class TracingServiceBinder : Binder() {

        fun getService(): ITracingServiceInterface {
            return object : ITracingServiceInterface {

                override fun dumpQueue() {
                    Log.i(SharedClassesSettings.TAG_TS, "Flushing queue, explicit request " + "through binder...")
                    this@TracingService.dumpQueue(true)
                }

                override fun enqueueTraceItem(ti: TraceItem) {
                    this@TracingService.enqueueTraceItem(ti)
                }

            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return TracingServiceBinder()
    }

    @SuppressLint("NewApi")
    override fun onDestroy() {
        Log.i(SharedClassesSettings.TAG_TS, "Destroying tracing service...")
        handlerDumpQueue!!.looper.quitSafely()
    }

    fun enqueueTraceItem(item: TraceItem) {
        traceQueue.add(item)
    }

    companion object {
        private val THREAD_PRIORITY_BACKGROUND = 10
        private val QUEUE_DUMP_LIMIT = 25
        private val DUMP_TIMEOUT: Long = 1000

        val ACTION_ENQUEUE_TRACE_ITEM = "enqueueTraceItem"
        val ACTION_DUMP_QUEUE = "dumpQueue"
        val ACTION_DUMP_QUEUE_SYNCHRONOUS = "dumpQueueSynchronous"
        val ACTION_NULL = "null"

        val EXTRA_ITEM_TYPE = "itemType"
        val EXTRA_TRACE_ITEM = "traceItem"

        val ITEM_TYPE_PATH_TRACKING = "pathTrackingItem"
    }


    private var DUMPER_STARTED = false
    private val traceQueue = LinkedBlockingQueue<TraceItem>()
    private var handlerDumpQueue: HandlerDumpQueue? = null
    private val communicator = ServerCommunicator(this)
}