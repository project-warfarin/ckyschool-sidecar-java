package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.plugin.BaseRemotingPluginSessionContext

data class RpcPacketContext(
        val arguments: RpcPacketMeta,
        val result: RpcPacketMeta
)

typealias RpcSessionContext = BaseRemotingPluginSessionContext<RpcPacketContext>