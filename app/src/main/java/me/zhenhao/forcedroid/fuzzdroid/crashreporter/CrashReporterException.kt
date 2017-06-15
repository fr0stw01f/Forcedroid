package me.zhenhao.forcedroid.fuzzdroid.crashreporter

/**
 * Created by tom on 6/8/17.
 */
class CrashReporterException : Exception {

    val originalClassName: String

    constructor(originalException: Throwable) : super(originalException.message) {
        this.originalClassName = originalException.javaClass.name
        stackTrace = originalException.stackTrace
    }


    internal constructor(message: String, className: String,
                         stackTrace: Array<StackTraceElement>) : super(message) {
        this.originalClassName = className
        setStackTrace(stackTrace)
    }

    companion object {
        private val serialVersionUID = 1047994013094918155L
    }

}