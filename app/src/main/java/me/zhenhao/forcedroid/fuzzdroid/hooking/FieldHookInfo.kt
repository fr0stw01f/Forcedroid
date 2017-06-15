package me.zhenhao.forcedroid.fuzzdroid.hooking

import java.util.regex.Pattern


/**
 * Created by tom on 6/8/17.
 */
class FieldHookInfo(private val fieldSignature: String) : HookInfo {
    val fieldSig: String = fieldSignature
    val className: String
    val fieldName: String

    var afterHook: AbstractFieldHookAfter? = null
        private set

    init {
        className = extractClassNameFromSignature()
        fieldName = extractFieldNameFromSignature()
    }

    override fun toString(): String {
        return "FieldHookInfo: " + fieldSig
    }

    private fun extractClassNameFromSignature(): String {
        val pattern = "<(.*):.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(fieldSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun extractFieldNameFromSignature(): String {
        val pattern = "<.*\\s(.*)>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(fieldSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    fun persistentHookAfter(fieldValue: Any) {
        afterHook = PersistentFieldHookAfter(fieldValue)
    }
}