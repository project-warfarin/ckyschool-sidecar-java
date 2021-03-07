package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader
import org.warfarin.ckyschool.sidecarj.remote.plugin.BaseRemotingPluginSessionContext

data class RpcPacketContext(
        var arguments: RpcPacketMeta?,
        var result: RpcPacketMeta?
)

class RpcSessionContext(plugin: Byte, header: CkyschoolProtocolHeader, context: RpcPacketContext)
    : BaseRemotingPluginSessionContext<RpcPacketContext, RpcPacketMeta, RpcPacketMeta>(plugin, header, context) {
    override fun input(): RpcPacketMeta? {
        return context?.arguments
    }

    override fun output(): RpcPacketMeta? {
        return context?.result
    }
}
