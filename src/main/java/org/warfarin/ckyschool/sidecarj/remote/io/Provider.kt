package org.warfarin.ckyschool.sidecarj.remote.io

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.util.concurrent.DefaultEventExecutorGroup
import io.netty.util.concurrent.EventExecutorGroup
import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader
import java.lang.Exception

class PluginProviderChannelInitializer(
        threadingOptions: ThreadingOptions,
        private val maxFrameLength: Int) : ChannelInitializer<SocketChannel>() {
    private var workerGroup: EventExecutorGroup = when (threadingOptions.javaClass) {
        FixedThreadingOptions::javaClass -> DefaultEventExecutorGroup(threadingOptions.coreThreads)
        FlexibleThreadingOptions::javaClass -> {
            val options = threadingOptions as FlexibleThreadingOptions
            DefaultEventExecutorGroup(options.coreThreads, options.threadFactory, options.backlog, options.rejectedExecutionHandler)
        }
        else -> throw Exception("Unsupported threading options: ${threadingOptions.javaClass.canonicalName}")
    }

    override fun initChannel(socketChannel: SocketChannel?) {
        socketChannel!!.pipeline()
                .addLast(LengthFieldBasedFrameDecoder(
                        maxFrameLength,
                        CkyschoolProtocolHeader.LENGTH_FIELD_OFFSET,
                        CkyschoolProtocolHeader.LENGTH_FIELD_SIZE))
                .addLast(CkyschoolPacketDecoder())
                .addLast(workerGroup, CkyschoolProviderInboundHandler())
                .addLast(CkyschoolPacketEncoder())
    }
}

class CkyschoolProviderInboundHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(context: ChannelHandlerContext, message: Any?) {
        TODO("Not yet implemented")
    }
}
