package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolRawMessage

interface RemotingPluginCodec<Meta> {
    fun decode(serializationProtocolId: Int, input: ByteArray, meta: Meta? = null): CkyschoolRawMessage
    fun encode(serializationProtocolId: Int, input: Any?, meta: Meta? = null): ByteArray?
}