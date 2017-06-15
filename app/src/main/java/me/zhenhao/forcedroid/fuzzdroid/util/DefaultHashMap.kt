package me.zhenhao.forcedroid.fuzzdroid.util

/**
 * Created by tom on 6/9/17.
 */
class DefaultHashMap<K, V>(protected var defaultValue: V) : HashMap<K, V>() {
    override fun get(k: K): V {
        return if (containsKey(k)) super.get(k)!! else defaultValue
    }

    companion object {
        private val serialVersionUID = -1099648480486824057L
    }
}