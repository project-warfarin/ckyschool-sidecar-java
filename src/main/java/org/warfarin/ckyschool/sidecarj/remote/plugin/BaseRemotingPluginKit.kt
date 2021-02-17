package org.warfarin.ckyschool.sidecarj.remote.plugin

abstract class BaseRemotingPluginKit<CodecMeta, DecodeOut>(
        private val codec: RemotingPluginCodec<CodecMeta, DecodeOut>,
        private val consumer: RemotingPluginConsumer,
        private val provider: RemotingPluginProvider) : SidecarRemotingPlugged {
}