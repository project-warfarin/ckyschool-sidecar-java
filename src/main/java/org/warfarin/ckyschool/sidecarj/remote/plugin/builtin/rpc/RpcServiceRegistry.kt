package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

class RpcServiceRegistry {
    fun getApiMeta(apiName: String): RpcApiMeta? {
        TODO("Not yet implemented")
    }

    companion object {
        val INSTANCE = RpcSerializerFactory()
    }
}

data class RpcServiceMeta(
        val targetInterfaceName: String,
        val apiMetaMap: Map<String, RpcApiMeta>
)

data class RpcApiMeta(
        val apiName: String,
        val enabled: Boolean,
        val paramHints: RpcPayloadSerializationHint,
        val timeoutMs: Int = 5000,
        val qpsLimit: Int?
)
