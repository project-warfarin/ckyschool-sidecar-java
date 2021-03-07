package org.warfarin.ckyschool.sidecarj.remote.plugin

interface RemotingPluginProvider<T> {
    fun serve(input: BaseRemotingPluginSessionContext<T>): BaseRemotingPluginSessionContext<T>
}