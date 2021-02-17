package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader

typealias BaseRemotingPluginKitGeneric = BaseRemotingPluginKit<*, *, *, *>

abstract class BaseRemotingPluginKit<T, Context: BaseRemotingPluginSessionContext<T>, CodecMeta, DecodeOut>(
        private val codec: RemotingPluginCodec<CodecMeta, DecodeOut>,
        private val consumer: RemotingPluginConsumer,
        private val provider: RemotingPluginProvider) : SidecarRemotingPlugged {
    // TODO implementation
}

abstract class BaseRemotingPluginSessionContext<T>(
        val pluginId: Byte,
        val header: CkyschoolProtocolHeader,
        val context: T?
)