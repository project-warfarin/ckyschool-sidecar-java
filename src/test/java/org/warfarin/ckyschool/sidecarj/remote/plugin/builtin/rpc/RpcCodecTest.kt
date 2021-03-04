package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Test
import org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.defaults.SerializationProtocolIds
import org.warfarin.ckyschool.sidecarj.util.fillWithBigEndianBytesFromInt
import org.warfarin.ckyschool.sidecarj.util.fillWithLittleEndianBytesFromInt
import java.text.SimpleDateFormat
import java.util.*

class RpcCodecTest {
    @Test
    fun should_decode_to_payload() {
        // setup
        val testRpcApiMeta = RpcApiMeta(
                apiUrl = "yjsnpi://blacktea-service/enchant",
                enabled = true,
                paramHints = RpcPayloadSerializationHint(
                        type = RpcPayloadSerializationHint.PAYLOAD_TYPE_REQUEST,
                        classNames = listOf(
                                RpcCodecTestArgType1::class.java.canonicalName,
                                RpcCodecTestArgType2::class.java.canonicalName
                        )
                ),
                resultHint = RpcPayloadSerializationHint.VOID_PARAMS,
                timeoutMs = 114514,
                qpsLimit = 810
        )
        RpcSerializerFactory.INSTANCE.register(RpcJsonSerializer())
        RpcServiceRegistry.INSTANCE.registerApiMeta(testRpcApiMeta, RpcApiRole.PROVIDER)

        // mock request
        val featureWord = SerializationProtocolIds.JSON and 0xFF        // request in json
        val callTraceId = "4cdbc040657a4847"
        val apiUrl = "yjsnpi://blacktea-service/enchant"
        val arg1 = RpcCodecTestArgType1(1, "arg1-锟斤拷-argfoobar", RpcCodecTestArgInner(3, listOf(4, 5)))
        val arg2 = RpcCodecTestArgType2(2L, true, SimpleDateFormat("yyyy-MM-dd").parse("1453-05-29"))
        val argJson = String.format("[%s,%s]", jacksonObjectMapper().writeValueAsString(arg1), jacksonObjectMapper().writeValueAsString(arg2))

        val apiUrlBytes = apiUrl.toByteArray()
        val argJsonBytes = argJson.toByteArray()
        val requestSize = 4 + 16 + 4 + apiUrlBytes.size + argJsonBytes.size

        val requestBuffer = ByteArray(requestSize)
        requestBuffer.fillWithBigEndianBytesFromInt(featureWord, 0)
        callTraceId.toByteArray().copyInto(requestBuffer, 4)
        requestBuffer.fillWithBigEndianBytesFromInt(apiUrlBytes.size, 4 + 16)
        apiUrlBytes.copyInto(requestBuffer, 4 + 16 + 4)
        argJsonBytes.copyInto(requestBuffer, 4 + 16 + 4 + apiUrlBytes.size)

        val rpcCodec = RpcCodec()
        val rpcPacketMeta = rpcCodec.decode(requestBuffer)
        println(jacksonObjectMapper().writeValueAsString(rpcPacketMeta))

        val arg1Reflection = rpcPacketMeta.payload!![0]!!.obj as RpcCodecTestArgType1
        val arg2Reflection = rpcPacketMeta.payload!![1]!!.obj as RpcCodecTestArgType2

        assert(arg1 == arg1Reflection)
        assert(arg2 == arg2Reflection)
    }
}

data class RpcCodecTestArgType1(
        @JsonProperty("intVal") val intVal: Int,
        @JsonProperty("stringVal") val stringVal: String,
        @JsonProperty("innerVal") val innerVal: RpcCodecTestArgInner
)

data class RpcCodecTestArgType2(
        @JsonProperty("longVal") val longVal: Long,
        @JsonProperty("booleanVal") val booleanVal: Boolean,
        @JsonProperty("dateVal") val dateVal: Date
)

data class RpcCodecTestArgInner(
        @JsonProperty("intVal") val intVal: Int,
        @JsonProperty("listVal") val listVal: List<Int>
)