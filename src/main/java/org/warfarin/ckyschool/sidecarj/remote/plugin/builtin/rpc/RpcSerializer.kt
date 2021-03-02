package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

interface RpcSerializer {
    val id: Int
    fun fromBytes(raw: ByteArray?, hint: RpcPayloadSerializationHint?): List<Any?>?
    fun <T> toBytes(obj: T?): ByteArray?
}

data class RpcPayloadSerializationHint(
        val type: Int,
        val classNames: List<String>
) {
    companion object {
        const val PAYLOAD_TYPE_REQUEST = 0
        const val PAYLOAD_TYPE_RESPONSE = 1

        val VOID_PARAMS = RpcPayloadSerializationHint(PAYLOAD_TYPE_REQUEST, listOf())
    }
}
