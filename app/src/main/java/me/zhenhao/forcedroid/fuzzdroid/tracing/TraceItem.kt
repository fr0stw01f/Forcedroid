package me.zhenhao.forcedroid.fuzzdroid.tracing

import android.os.Parcel
import android.os.Parcelable
import me.zhenhao.forcedroid.fuzzdroid.networkconnection.IClientRequest



/**
 * Created by tom on 6/8/17.
 */
abstract class TraceItem : Parcelable, IClientRequest {

    var lastExecutedStatement: Int = 0
        private set
    private var globalLastExecutedStatement: Int = 0

    protected constructor() : super()

    @JvmOverloads protected constructor(lastExecutedStatement: Int, globalLastExecutedStatement: Int = -1) {
        this.lastExecutedStatement = lastExecutedStatement
        this.globalLastExecutedStatement = globalLastExecutedStatement
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        parcel.writeInt(lastExecutedStatement)
        parcel.writeInt(globalLastExecutedStatement)
    }

    open protected fun readFromParcel(parcel: Parcel) {
        this.lastExecutedStatement = parcel.readInt()
        this.globalLastExecutedStatement = parcel.readInt()
    }

    companion object {
        private val serialVersionUID = 5704527703779833243L
    }

}