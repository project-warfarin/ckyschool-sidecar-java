package org.warfarin.ckyschool.sidecarj.remote.plugin

interface RemotingPluginConsumer<T> {
    fun request(input: BaseRemotingPluginSessionContext<T>): BaseRemotingPluginSessionContext<T>
}