package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolRawMessage
import org.warfarin.ckyschool.sidecarj.remote.plugin.RemotingPluginCodec

class RpcCodec : RemotingPluginCodec<RpcApiMeta> {
    override fun decode(serializationProtocolId: Int, input: ByteArray, meta: RpcApiMeta?): CkyschoolRawMessage {
        val serializer = RpcSerializerFactory.INSTANCE.get(serializationProtocolId)!!
        TODO("Not yet implemented")
    }

    override fun encode(serializationProtocolId: Int, input: Any?, meta: RpcApiMeta?): ByteArray? {
        TODO("Not yet implemented")
    }
}