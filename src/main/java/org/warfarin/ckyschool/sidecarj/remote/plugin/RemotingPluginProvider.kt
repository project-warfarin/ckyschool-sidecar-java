package org.warfarin.ckyschool.sidecarj.remote.plugin

import org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolRawMessage

interface RemotingPluginProvider {
    fun serve(input: CkyschoolRawMessage): CkyschoolRawMessage
}