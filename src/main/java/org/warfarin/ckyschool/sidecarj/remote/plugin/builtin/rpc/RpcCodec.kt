package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.plugin.RemotingPluginCodec
import org.warfarin.ckyschool.sidecarj.util.fillWithBigEndianBytesFromInt
import org.warfarin.ckyschool.sidecarj.util.intFromBigEndianBytes

class RpcCodec : RemotingPluginCodec<RpcPacketMeta, RpcPacketMeta> {
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
                payload,
                apiMeta
        )
    }

    override fun encode(meta: RpcPacketMeta, input: Any?): ByteArray? {
        val serializer = RpcSerializerFactory.INSTANCE.get(meta.serializationProtocolId)

        val featureWord = (meta.serializationProtocolId and 0xFF) or ((meta.packetType and 0x1) shl 16)
        val calltraceId = meta.calltraceId
        val apiUrlBytes = meta.apiUrl.toByteArray()
        val apiUrlLength = apiUrlBytes.size

        val hint = when (meta.packetType) {
            RpcPacketMeta.PACKET_TYPE_REQUEST -> meta.apiMeta.paramHints
            RpcPacketMeta.PACKET_TYPE_RESPONSE -> meta.apiMeta.resultHint
            else -> throw Exception("RPC packet type ${meta.packetType} not recognized")
        }
        val actualInput = when (meta.packetType) {
            RpcPacketMeta.PACKET_TYPE_REQUEST -> meta.payload?.map { it?.obj }
            RpcPacketMeta.PACKET_TYPE_RESPONSE -> meta.payload?.get(0)?.obj
            else -> throw Exception("RPC packet type ${meta.packetType} not recognized")
        }
        val payload = serializer!!.toBytes(actualInput, hint)
        val payloadLength = payload!!.size

        val rpcPacketLength = 4 + 16 + 4 + apiUrlLength + payloadLength
        val packetBuffer = ByteArray(rpcPacketLength)
        packetBuffer.fillWithBigEndianBytesFromInt(featureWord, 0)
        calltraceId.toByteArray().copyInto(packetBuffer, 4)
        packetBuffer.fillWithBigEndianBytesFromInt(apiUrlLength, 4 + 16)
        apiUrlBytes.copyInto(packetBuffer, 4 + 16 + 4)
        payload.copyInto(packetBuffer, 4 + 16 + 4 + apiUrlLength)

        return packetBuffer
    }
}
