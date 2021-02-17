package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Test
import java.util.stream.IntStream

class RpcJsonSerializerTest {
    private val paramString =
            "[{\"integerValue\":114514,\"stringValue\":\"YJSNPI\"}, {\"doubleValue\":810.1919,\"pojoA\":{\"integerValue\":364364,\"stringValue\":\"MUR\"}}, 1145141919]"
    private val serializer = RpcJsonSerializer()

    private val hints = RpcPayloadSerializationHint(
            RpcPayloadSerializationHint.PAYLOAD_TYPE_REQUEST,
            listOf(
                    PojoA::class.java.canonicalName,
                    PojoB::class.java.canonicalName,
                    java.lang.Long::class.java.canonicalName
            )
    )

    @Test
    fun should_deserialize_multiple_parameters() {
        for (hint in hints.classNames) {
            println(hint)
        }

        val objects = serializer.fromBytes(paramString.toByteArray(), hints)!!
        println(String(jacksonObjectMapper().writeValueAsBytes(objects)))

        val pojoA = objects[0] as PojoA
        val pojoB = objects[1] as PojoB
        val longValue = objects[2] as Long

        assert(pojoA.integerValue == 114514)
        assert(pojoA.stringValue == "YJSNPI")
        assert(pojoB.pojoA.integerValue == 364364)
        assert(pojoB.pojoA.stringValue == "MUR")
        assert(longValue == 1145141919L)
    }

    @Test
    fun deserialize_speedrun() {
        for (hint in hints.classNames) {
            println(hint)
        }

        val paramStringTemplate =
                "[{\"integerValue\":%d,\"stringValue\":\"YJSNPI-%d\"}, {\"doubleValue\":810.1919,\"pojoA\":{\"integerValue\":%d,\"stringValue\":\"MUR-%d\"}}, 1145141919]"
        val rangeBound = 10000
        val paramByteArrays = IntStream.range(0, rangeBound).mapToObj { String.format(paramStringTemplate, it, it, it, it).toByteArray() }

        val gmtBegin = System.currentTimeMillis()
        paramByteArrays.forEach { serializer.fromBytes(it, hints) }
        val gmtEnd = System.currentTimeMillis()
        println("Time elapsed: ${gmtEnd - gmtBegin}ms")
        println("Average: ${(gmtEnd - gmtBegin).toDouble() / rangeBound}ms")
    }
}

data class PojoA(
        val integerValue: Int,
        val stringValue: String
)

data class PojoB(
        val doubleValue: Double,
        val pojoA: PojoA
)