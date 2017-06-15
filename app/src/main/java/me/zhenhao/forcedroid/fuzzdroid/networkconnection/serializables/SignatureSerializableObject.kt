package me.zhenhao.forcedroid.fuzzdroid.networkconnection.serializables

import java.io.Serializable

/**
 * Created by tom on 6/9/17.
 */
class SignatureSerializableObject(val encodedCertificate: ByteArray) : Serializable {
    companion object {
        private val serialVersionUID = -1089353033691760402L
    }
}