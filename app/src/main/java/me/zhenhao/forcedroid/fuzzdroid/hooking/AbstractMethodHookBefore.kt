package me.zhenhao.forcedroid.fuzzdroid.hooking

import me.zhenhao.forcedroid.fuzzdroid.hooking.AbstractMethodHook

/**
 * Created by tom on 6/8/17.
 */
abstract class AbstractMethodHookBefore : AbstractMethodHook() {

    abstract fun getParamValuesToReplace(): Set<Pair<Int, Any>>
}