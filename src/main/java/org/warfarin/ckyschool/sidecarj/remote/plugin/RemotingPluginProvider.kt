package org.warfarin.ckyschool.sidecarj.remote.plugin

interface RemotingPluginProvider<T, In, Out, Context: BaseRemotingPluginSessionContext<*, *, *>> {
    fun serve(context: Context)
}