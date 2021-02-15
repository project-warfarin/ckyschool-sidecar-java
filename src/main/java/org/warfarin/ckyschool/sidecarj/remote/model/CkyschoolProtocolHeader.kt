package org.warfarin.ckyschool.sidecarj.remote.model

import io.netty.buffer.ByteBuf
import kotlin.math.min

data class CkyschoolProtocolHeader(
        val magic: Int,
        val majorVersion: Byte,
        val minorVersion: Byte,
        val patchVersion: Byte,
        val pluginId: Byte,
        val features: List<CkyschoolRemotingFeatures>,
        val payloadLength: Long
) {
    companion object {
        const val RAW_HEADER_LENGTH = 8 + 4 + 4
        const val LENGTH_FIELD_SIZE = 4
        const val LENGTH_FIELD_OFFSET = 8 + 4

        fun fromBytes(input: ByteBuf): CkyschoolProtocolHeader? {
            if (input.readableBytes() < RAW_HEADER_LENGTH) {
                return null
            }
            return CkyschoolProtocolHeader(
                magic = input.readInt(),
                majorVersion = input.readByte(),
                minorVersion = input.readByte(),
                patchVersion = input.readByte(),
                pluginId = input.readByte(),
                features = CkyschoolRemotingFeatures.featuresFromInt(input.readInt()),
                payloadLength = input.readUnsignedInt()
            )
        }
    }

    fun toBytes(output: ByteBuf) {
        output.writeInt(magic)
        output.writeBytes(byteArrayOf(majorVersion, minorVersion, patchVersion, pluginId))
        output.writeInt(CkyschoolRemotingFeatures.featuresToInt(features))
        output.writeInt(payloadLength.and(0xFFFFFFFF).toInt())
    }
}

enum class CkyschoolRemotingFeatures {
    HELO,
    PING,
    PAYLOAD;

    companion object {
        fun featuresFromInt(deflated: Int): List<CkyschoolRemotingFeatures> {
            // TODO implementation
            return listOf()
        }

        fun featuresToInt(features: List<CkyschoolRemotingFeatures>): Int {
            // TODO implementation
            return 0
        }
    }
}