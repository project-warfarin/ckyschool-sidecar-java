package org.warfarin.ckyschool.sidecarj.remote.model

data class CkyschoolRawMessage(
        val header: CkyschoolProtocolHeader,
        val payload: Any?
)
