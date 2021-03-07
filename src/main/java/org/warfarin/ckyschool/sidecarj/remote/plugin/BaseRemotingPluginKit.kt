package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader

typealias BaseRemotingPluginKitGeneric = BaseRemotingPluginKit<*, *, *, *, *, *>

abstract class BaseRemotingPluginKit<T, In, Out, Context: BaseRemotingPluginSessionContext<T, In, Out>, CodecMeta, DecodeOut>(
        private val codec: RemotingPluginCodec<CodecMeta, DecodeOut>,
        private val consumer: RemotingPluginConsumer<T, In, Out, Context>,
        private val provider: RemotingPluginProvider<T, In, Out, Context>) : SidecarRemotingPlugged {
    // TODO implementation
}

abstract class BaseRemotingPluginSessionContext<T, In, Out>(
        val pluginId: Byte,
        val header: CkyschoolProtocolHeader,
        val context: T?
) {
    abstract fun input(): In?
    abstract fun output(): Out?
}