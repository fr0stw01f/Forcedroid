package me.zhenhao.forcedroid.fuzzdroid.networkconnection.serializables

import me.zhenhao.forcedroid.fuzzdroid.FileFormat
import java.io.Serializable

/**
 * Created by tom on 6/9/17.
 */
class FileFuzzingSerializableObject(val fileFormat: FileFormat, val storageMode: Int) : Serializable {

    override fun toString(): String {
        return String.format("file format: %s | mode: %d", fileFormat, storageMode)
    }

    companion object {
        private val serialVersionUID = 8055219086869125404L
    }
}