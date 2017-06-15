package me.zhenhao.forcedroid.fuzzdroid.tracing

/**
 * Created by tom on 6/8/17.
 */
internal interface ITracingServiceInterface {

    fun dumpQueue()

    fun enqueueTraceItem(ti: TraceItem)

}