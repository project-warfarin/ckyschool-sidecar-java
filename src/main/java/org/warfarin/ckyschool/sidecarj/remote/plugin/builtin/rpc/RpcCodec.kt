package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.plugin.RemotingPluginCodec
import org.warfarin.ckyschool.sidecarj.util.intFromLittleEndianBytes

class RpcCodec : RemotingPluginCodec<Void, RpcPacketMeta> {
    override fun decode(input: ByteArray): RpcPacketMeta {
        val serializationProtocolId = input.intFromLittleEndianBytes(RpcPacketFieldHelper.RANGE_SERIALIZATION_PROTOCOL_ID)
        val packetType = input.intFromLittleEndianBytes(RpcPacketFieldHelper.RANGE_PACKET_TYPE)
        val calltraceId = String(input.sliceArray(RpcPacketFieldHelper.RANGE_CALLTRACE_ID))
        val apiUrlLength = input.intFromLittleEndianBytes(RpcPacketFieldHelper.RANGE_API_URL_LENGTH)
        val apiUrl = String(input.sliceArray(RpcPacketFieldHelper.RANGE_API_URL(apiUrlLength)))
        val actualHeaderLength = RpcPacketFieldHelper.OFFSET_API_URL + apiUrlLength

        val serializer = RpcSerializerFactory.INSTANCE.get(serializationProtocolId)!!
        val role = when (packetType) {
            RpcPacketMeta.PACKET_TYPE_REQUEST -> RpcApiRole.PROVIDER
            RpcPacketMeta.PACKET_TYPE_RESPONSE -> RpcApiRole.CONSUMER
            else -> throw Exception("RPC packet type $packetType not recognized")
        }
        val apiMeta = RpcServiceRegistry.INSTANCE.getApiMeta(apiUrl, role)!!
        val hint = when (packetType) {
            RpcPacketMeta.PACKET_TYPE_REQUEST -> apiMeta.paramHints
            RpcPacketMeta.PACKET_TYPE_RESPONSE -> apiMeta.resultHint
            else -> throw Exception("RPC packet type $packetType not recognized")
        }

        val payload = hint.let {
            if (it == RpcPayloadSerializationHint.VOID_PARAMS) {
                RpcObjectMeta.VOID_ARGUMENTS
            }
            val payloadObjects = serializer.fromBytes(input.sliceArray(IntRange(actualHeaderLength, input.size)), hint)
            IntRange(0, it.classNames.size).map { index ->
                RpcObjectMeta(it.classNames[index], payloadObjects?.get(index))
            }
        }

        return RpcPacketMeta(
                serializationProtocolId,
                packetType,
                calltraceId,
                apiUrl,
                payload
        )
    }

    override fun encode(serializationProtocolId: Int, input: Any?, meta: Void?): ByteArray? {
        val serializer = RpcSerializerFactory.INSTANCE.get(serializationProtocolId)!!
        return serializer.toBytes(input)
    }
}
