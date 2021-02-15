package org.warfarin.ckyschool.sidecarj.remote.io

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToByteEncoder
import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader

class CkyschoolPacketDecoder : ByteToMessageDecoder() {
    override fun decode(context: ChannelHandlerContext?, input: ByteBuf, output: MutableList<Any>?) {
        val header = CkyschoolProtocolHeader.fromBytes(input)
        // TODO implementation
        // 1. Find the payload decoder for corresponding plugin
        // 2. Decode the payload
    }
}

class CkyschoolPacketEncoder : MessageToByteEncoder<Any?>() {
    override fun encode(context: ChannelHandlerContext?, input: Any?, output: ByteBuf) {
        TODO("Not yet implemented")
    }
}
