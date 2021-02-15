package org.warfarin.ckyschool.sidecarj.remote.io

import io.netty.util.concurrent.RejectedExecutionHandler
import io.netty.util.concurrent.RejectedExecutionHandlers
import java.util.concurrent.ThreadFactory

interface ThreadingOptions {
    val coreThreads: Int
}

data class FixedThreadingOptions(override val coreThreads: Int) : ThreadingOptions

data class FlexibleThreadingOptions(
        override val coreThreads: Int,
        val backlog: Int,
        val threadFactory: ThreadFactory,
        val rejectedExecutionHandler: RejectedExecutionHandler = RejectedExecutionHandlers.reject()
) : ThreadingOptions