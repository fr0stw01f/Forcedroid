package me.zhenhao.forcedroid.fuzzdroid.dynamiccfg

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by tom on 6/9/17.
 */
class MethodEnterItem : AbstractDynamicCFGItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    override fun toString(): String {
        return "Method enter: " + lastExecutedStatement
    }

    companion object {

        private val serialVersionUID = -8382002494703671501L

        val CREATOR: Parcelable.Creator<MethodEnterItem> = object : Parcelable.Creator<MethodEnterItem> {

            override fun createFromParcel(parcel: Parcel): MethodEnterItem {
                val mci = MethodEnterItem()
                mci.readFromParcel(parcel)
                return mci
            }

            override fun newArray(size: Int): Array<MethodEnterItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}