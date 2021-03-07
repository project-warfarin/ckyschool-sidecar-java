package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader
import org.warfarin.ckyschool.sidecarj.remote.plugin.BaseRemotingPluginSessionContext

data class RpcPacketContext(
        var arguments: RpcPacketMeta?,
        var result: RpcPacketMeta?
)

class RpcSessionContext(plugin: Byte, header: CkyschoolProtocolHeader, context: RpcPacketContext)
    : BaseRemotingPluginSessionContext<RpcPacketContext, RpcPacketMeta, RpcPacketMeta>(plugin, header, context) {
    override fun getInput(): RpcPacketMeta? {
        return context?.arguments
    }

    override fun setInput(input: RpcPacketMeta?) {
        if (context != null) {
            context.arguments = input
        }
    }

    override fun getOutput(): RpcPacketMeta? {
        return context?.result
    }

    override fun setOutput(output: RpcPacketMeta?) {
        if (context != null) {
            context.result = output
        }
    }
}
