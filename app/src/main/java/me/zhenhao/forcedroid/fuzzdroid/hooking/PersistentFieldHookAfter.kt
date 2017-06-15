package me.zhenhao.forcedroid.fuzzdroid.hooking

/**
 * Created by tom on 6/8/17.
 */
class PersistentFieldHookAfter(private val newValue: Any) : AbstractFieldHookAfter() {

    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

    override fun getNewValue(): Any {
        return newValue
    }
}