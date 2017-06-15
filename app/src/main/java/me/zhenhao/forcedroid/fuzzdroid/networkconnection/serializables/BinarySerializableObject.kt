package me.zhenhao.forcedroid.fuzzdroid.networkconnection.serializables

import me.zhenhao.forcedroid.fuzzdroid.networkconnection.IClientRequest
import java.io.Serializable

/**
 * Created by tom on 6/9/17.
 */
class BinarySerializableObject(val binaryData: ByteArray) : Serializable, IClientRequest {
    companion object {
        private val serialVersionUID = -1043817079853486666L
    }

}