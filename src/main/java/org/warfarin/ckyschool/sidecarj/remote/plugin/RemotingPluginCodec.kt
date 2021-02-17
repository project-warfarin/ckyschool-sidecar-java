package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolRawMessage

interface RemotingPluginCodec<EncodeMeta, DecodeOut> {
    fun decode(input: ByteArray): DecodeOut
    fun encode(serializationProtocolId: Int, input: Any?, meta: EncodeMeta? = null): ByteArray?
}