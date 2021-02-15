package org.warfarin.ckyschool.sidecarj.remote.plugin

interface SidecarRemotingPlugged {
    /**
     * Unique ID for every remoting plugin.
     * @see org.warfarin.ckyschool.sidecarj.remote.model.CkyschoolProtocolHeader.pluginId
     */
    val id: Byte
}