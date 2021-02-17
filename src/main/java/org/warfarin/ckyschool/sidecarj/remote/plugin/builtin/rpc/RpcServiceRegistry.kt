package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

class RpcServiceRegistry {
    fun getApiMeta(apiUrl: String, role: RpcApiRole): RpcApiMeta? {
        TODO("Not yet implemented")
    }

    companion object {
        val INSTANCE = RpcServiceRegistry()
    }
}

data class RpcServiceMeta(
        val targetInterfaceName: String,
        val apiMetaMap: Map<String, RpcApiMeta>
)

data class RpcApiMeta(
        val apiUrl: String,
        val enabled: Boolean,
        val paramHints: RpcPayloadSerializationHint,
        val resultHint: RpcPayloadSerializationHint,
        val timeoutMs: Int = 5000,
        val qpsLimit: Int?
)

enum class RpcApiRole {
    CONSUMER,
    PROVIDER
}
