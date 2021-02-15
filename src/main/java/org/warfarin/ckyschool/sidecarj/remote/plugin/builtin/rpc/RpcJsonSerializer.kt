package org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.rpc

import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.warfarin.ckyschool.sidecarj.remote.plugin.builtin.defaults.SerializationProtocolIds

class RpcJsonSerializer : RpcSerializer {
    override val id: Int
        get() = SerializationProtocolIds.JSON

    private val objectMapper = jacksonObjectMapper().registerModule(AfterburnerModule())

    @Suppress("NAME_SHADOWING")
    override fun fromBytes(raw: ByteArray?, hint: RpcPayloadSerializationHint?): List<Any?>? {
        if (raw == null) {
            if (hint!!.classNames.isNullOrEmpty()) {
                return null
            } else {
                throw RuntimeException("Null content while ${hint.classNames.size} elements expected")
            }
        }

        val json = objectMapper.readValue(raw, List::class.java)

        val classes = hint!!.classNames.map {
            Class.forName(it)
        }

        val jsonIterator = json.asIterable().iterator()
        val classIterator = classes.iterator()
        val results: MutableList<Any?> = mutableListOf()
        while (classIterator.hasNext()) {
            if (!jsonIterator.hasNext()) {
                throw RuntimeException("JSON array exhausted: ${classes.size} element(s)")
            }
            val clazz = classIterator.next()
            val json = jsonIterator.next()
            val obj = objectMapper.convertValue(json, clazz)
            results.add(obj)
        }
        return results
    }

    override fun <T> toBytes(obj: T?): ByteArray? {
        return objectMapper.writeValueAsBytes(obj)
    }
}
