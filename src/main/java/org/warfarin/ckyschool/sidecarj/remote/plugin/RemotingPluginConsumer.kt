package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolRawMessage

interface RemotingPluginConsumer {
    fun request(input: CkyschoolRawMessage): CkyschoolRawMessage
}