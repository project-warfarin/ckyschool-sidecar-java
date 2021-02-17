package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

data class RpcPacketMeta(
        val serializationProtocolId: Int,
        val packetType: Int,
        val calltraceId: String,
        val apiUrl: String,
        val payload: List<RpcObjectMeta?>?
) {
    companion object {
        const val PACKET_TYPE_REQUEST = RpcPayloadSerializationHint.PAYLOAD_TYPE_REQUEST
        const val PACKET_TYPE_RESPONSE = RpcPayloadSerializationHint.PAYLOAD_TYPE_RESPONSE
    }
}

data class RpcObjectMeta(
        val className: String,
        val obj: Any?
) {
    companion object {
        val VOID_ARGUMENTS: List<RpcObjectMeta> = listOf()
    }
}
