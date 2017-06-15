package me.zhenhao.forcedroid.fuzzdroid.hooking

import java.io.Serializable;

/**
 * Created by tom on 6/8/17.
 */
class DummyValue : Serializable {

    override fun toString(): String {
        return "<DUMMY>"
    }

    companion object {
        private val serialVersionUID = -3619572732272288459L
    }
}