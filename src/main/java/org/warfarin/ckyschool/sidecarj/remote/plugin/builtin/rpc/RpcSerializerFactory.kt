package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import java.lang.Exception

class RpcSerializerFactory {
    private val serializers: MutableMap<Int, RpcSerializer> = mutableMapOf()

    fun register(serializer: RpcSerializer, allowOverride: Boolean = false) {
        synchronized(this) {
            if (!allowOverride && serializers.containsKey(serializer.id)) {
                throw RpcSerializerConflictError(serializer.id, "RPC serializer register failed: conflicted ID=${serializer.id}")
            }
            serializers[serializer.id] = serializer
        }
    }

    fun get(id: Int): RpcSerializer? {
        return serializers[id]
    }

    companion object {
        val INSTANCE = RpcSerializerFactory()
    }
}

class RpcSerializerConflictError(val id: Int, message: String): Exception(message)