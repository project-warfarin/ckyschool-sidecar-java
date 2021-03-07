package org.warfarin.ckyschool.sidecarj.remote.plugin

interface RemotingPluginConsumer<T, In, Out, Context: BaseRemotingPluginSessionContext<T, In, Out>> {
    fun request(input: Context)
}