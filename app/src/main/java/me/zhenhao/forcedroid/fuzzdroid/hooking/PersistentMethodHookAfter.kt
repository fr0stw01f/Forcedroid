package me.zhenhao.forcedroid.fuzzdroid.hooking

/**
 * Created by tom on 6/8/17.
 */
class PersistentMethodHookAfter(private val returnValue: Any) : AbstractMethodHookAfter() {

    override fun getReturnValue(): Any? {
        return returnValue
    }

    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

}