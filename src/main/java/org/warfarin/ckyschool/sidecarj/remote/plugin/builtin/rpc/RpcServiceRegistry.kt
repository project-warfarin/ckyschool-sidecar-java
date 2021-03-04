package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import java.lang.Exception

class RpcServiceRegistry {
    private val consumerMeta: MutableMap<String, RpcApiMeta> = mutableMapOf()
    private val providerMeta: MutableMap<String, RpcApiMeta> = mutableMapOf()

    fun registerApiMeta(meta: RpcApiMeta, role: RpcApiRole, allowOverride: Boolean = false) {
        synchronized(this) {
            val targetMap = when (role) {
                RpcApiRole.CONSUMER -> consumerMeta
                RpcApiRole.PROVIDER -> providerMeta
            }
            if (!allowOverride && targetMap.containsKey(meta.apiUrl)) {
                throw RpcApiMetaConflictError(meta.apiUrl, "RPC API meta register failed: conflicted apiUrl=${meta.apiUrl}")
            }
            targetMap[meta.apiUrl] = meta
        }
    }

    fun getApiMeta(apiUrl: String, role: RpcApiRole): RpcApiMeta? {
        val targetMap = when (role) {
            RpcApiRole.CONSUMER -> consumerMeta
            RpcApiRole.PROVIDER -> providerMeta
        }
        return targetMap[apiUrl]
    }

    companion object {
        val INSTANCE = RpcServiceRegistry()
    }
}

class RpcApiMetaConflictError(val apiUrl: String, message: String): Exception(message)

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
