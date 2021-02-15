package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

interface RpcSerializer {
    val id: Int
    fun fromBytes(raw: ByteArray?, hint: RpcPayloadSerializationHint?): List<Any?>?
    fun <T> toBytes(obj: T?): ByteArray?
}

data class RpcPayloadSerializationHint(
        val classNames: List<String>
) {
    companion object {
        val VOID_PARAMS = RpcPayloadSerializationHint(listOf())
    }
}
