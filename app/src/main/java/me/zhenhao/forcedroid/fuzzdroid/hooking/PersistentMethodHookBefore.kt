package me.zhenhao.forcedroid.fuzzdroid.hooking

/**
 * Created by tom on 6/8/17.
 */
class PersistentMethodHookBefore(private val paramValuePair: Set<Pair<Int, Any>>) : AbstractMethodHookBefore() {

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>> {
        return paramValuePair
    }


    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

}