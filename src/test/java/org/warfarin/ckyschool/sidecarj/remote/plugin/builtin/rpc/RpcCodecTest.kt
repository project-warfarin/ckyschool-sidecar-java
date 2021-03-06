package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.warfarin.ckyschool.sidecarj.remote.plugin.RemotingPluginCodec
import org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.defaults.SerializationProtocolIds
import org.warfarin.ckyschool.sidecarj.util.fillWithBigEndianBytesFromInt
import java.text.SimpleDateFormat
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RpcCodecTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            RpcSerializerFactory.INSTANCE.register(RpcJsonSerializer())
            RpcServiceRegistry.INSTANCE.registerApiMeta(testRpcApiMeta, RpcApiRole.PROVIDER)
        }
    }

    @Test
    fun test_001_should_decode_to_payload() {
        val featureWord = SerializationProtocolIds.JSON and 0xFF
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

    @Test
    fun test_002_should_encode_to_payload() {
        val callTraceId = "4cdbc040657a4847"
        val apiUrl = "yjsnpi://blacktea-service/enchant"
        val arg1 = RpcCodecTestArgType1(1, "arg1-锟斤拷-argfoobar", RpcCodecTestArgInner(3, listOf(4, 5)))
        val arg2 = RpcCodecTestArgType2(2L, true, SimpleDateFormat("yyyy-MM-dd").parse("1453-05-29"))
        val rpcPacketMeta = RpcPacketMeta(
                serializationProtocolId = SerializationProtocolIds.JSON,
                packetType = RpcPacketMeta.PACKET_TYPE_REQUEST,
                calltraceId = callTraceId,
                apiUrl = apiUrl,
                apiMeta = testRpcApiMeta,
                payload = listOf(
                        RpcObjectMeta(
                                className = arg1::class.java.canonicalName,
                                obj = arg1
                        ),
                        RpcObjectMeta(
                                className = arg2::class.java.canonicalName,
                                obj = arg2
                        )
                )
        )

        val rpcCodec = RpcCodec()
        val encodeResult = rpcCodec.encode(rpcPacketMeta, RemotingPluginCodec.OMITTED_INPUT)
        val decodeResult = rpcCodec.decode(encodeResult!!)
        println(jacksonObjectMapper().writeValueAsString(decodeResult))
        assert(rpcPacketMeta == decodeResult)
    }
}

// setup
private val testRpcApiMeta = RpcApiMeta(
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