package org.warfarin.ckyschool.sidecarj.remote.plugin

abstract class BaseRemotingPluginKit<CodecMeta>(
        private val codec: RemotingPluginCodec<CodecMeta>,
        private val consumer: RemotingPluginConsumer,
        private val provider: RemotingPluginProvider) : SidecarRemotingPlugged {
}