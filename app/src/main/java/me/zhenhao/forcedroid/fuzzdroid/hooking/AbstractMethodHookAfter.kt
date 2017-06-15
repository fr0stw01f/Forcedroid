package me.zhenhao.forcedroid.fuzzdroid.hooking

/**
 * Created by tom on 6/8/17.
 */
abstract class AbstractMethodHookAfter : AbstractMethodHook() {

    abstract fun getReturnValue(): Any?
}