package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader
import org.warfarin.ckyschool.sidecarj.remote.plugin.BaseRemotingPluginSessionContext

class RpcContextView(pluginId: Byte, header: CkyschoolProtocolHeader)
    : BaseRemotingPluginSessionContext<Void, Void, Void>(pluginId, header, null) {
    override fun getInput(): Void? {
        throw UnsupportedOperationException()
    }

    override fun setInput(input: Void?) {
        throw UnsupportedOperationException()
    }

    override fun getOutput(): Void? {
        throw UnsupportedOperationException()
    }

    override fun setOutput(output: Void?) {
        throw UnsupportedOperationException()
    }
}

abstract class RpcArgumentInterceptor {
    abstract val priority: Int
    abstract fun processArguments(arguments: List<RpcObjectMeta?>?, context: RpcContextView): List<RpcObjectMeta?>?
}

abstract class UserDefinedRpcArgumentInterceptor : RpcArgumentInterceptor()
internal abstract class BuiltinRpcArgumentInterceptor : RpcArgumentInterceptor()

internal open class RpcArgumentInterceptorPipeline(fromUser: List<UserDefinedRpcArgumentInterceptor>, builtinInitials: List<BuiltinRpcArgumentInterceptor>) {
    private val highPriorityExternals: MutableList<UserDefinedRpcArgumentInterceptor> =
            fromUser.filter { it.priority < 0 }.sortedBy { it.priority }.toMutableList()
    private val lowPriorityExternals: MutableList<UserDefinedRpcArgumentInterceptor> =
            fromUser.filter { it.priority >= 0 }.sortedBy { it.priority }.toMutableList()
    private val builtins: MutableList<BuiltinRpcArgumentInterceptor> = builtinInitials.sortedBy { it.priority }.toMutableList()

    open fun trigger(context: RpcSessionContext) {
        val contextView = RpcContextView(context.pluginId, context.header)
        var arg = context.getInput()?.payload
        highPriorityExternals.forEach { arg = it.processArguments(arg, contextView) }
        builtins.forEach { arg = it.processArguments(arg, contextView) }
        lowPriorityExternals.forEach { arg = it.processArguments(arg, contextView) }
        if (context.getInput() != null) {
            context.getInput()!!.payload = arg
        }
    }

    companion object {
        val TRIVIAL = TrivialRpcArgumentInterceptorPipeline()
    }
}

internal class TrivialRpcArgumentInterceptorPipeline : RpcArgumentInterceptorPipeline(listOf(), listOf()) {
    override fun trigger(context: RpcSessionContext) {
        // do nothing
    }
}