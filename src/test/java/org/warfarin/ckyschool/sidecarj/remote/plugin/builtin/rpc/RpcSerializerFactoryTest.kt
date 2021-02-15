package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import org.junit.Test
import org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.defaults.SerializationProtocolIds

class RpcSerializerFactoryTest {
    @Test
    fun should_get_json_serializer() {
        RpcSerializerFactory.INSTANCE.register(RpcJsonSerializer())

        val serializer = RpcSerializerFactory.INSTANCE.get(SerializationProtocolIds.JSON)!!
        println(serializer::class.java.canonicalName)
        assert(serializer is RpcJsonSerializer)
    }
}