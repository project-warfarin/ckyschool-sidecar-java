package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.plugin.RemotingPluginCodec
import org.warfarin.ckyschool.sidecarj.util.intFromBigEndianBytes
import org.warfarin.ckyschool.sidecarj.util.intFromLittleEndianBytes

class RpcCodec : RemotingPluginCodec<Void, RpcPacketMeta> {
    override fun decode(input: ByteArray): RpcPacketMeta {
        val featureWord = input.intFromBigEndianBytes(0, 4)
        val serializationProtocolId = featureWord and 0xFF
        val packetType = (featureWord ushr 16) and 0x1

        val calltraceId = String(input.sliceArray(IntRange(4, 4 + 16 - 1)))
        val apiUrlLength =  input.intFromBigEndianBytes(4 + 16, 4 + 16 + 4)
        val apiUrl = String(input.sliceArray(IntRange(4 + 16 + 4, 4 + 16 + 4 + apiUrlLength - 1)))

        val actualHeaderLength = 4 + 16 + 4 + apiUrlLength

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
            val payloadObjects = serializer.fromBytes(input.sliceArray(IntRange(actualHeaderLength, input.lastIndex)), hint)
            IntRange(0, it.classNames.lastIndex).map { index ->
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

    override fun encode(serializationProtocolId: Int, packetType: Int, input: Any?, meta: Void?): ByteArray? {
//        val serializer = RpcSerializerFactory.INSTANCE.get(serializationProtocolId)!!
//        return serializer.toBytes(input)
        TODO()
    }
}
