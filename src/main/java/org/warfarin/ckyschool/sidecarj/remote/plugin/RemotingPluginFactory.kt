@file:Suppress("unused")

package org.warfarin.ckyschool.sidecarj.remote.plugin

import java.lang.Exception

class RemotingPluginFactory {
    private val plugins: MutableMap<Byte, BaseRemotingPluginKitGeneric> = mutableMapOf()

    @Throws(PluginRegisterConflictError::class)
    fun register(plugin: BaseRemotingPluginKitGeneric, allowOverride: Boolean = false) {
        synchronized(this) {
            if (!allowOverride && plugins.containsKey(plugin.id)) {
                throw PluginRegisterConflictError(plugin.id, "Plugin register failed: conflicted ID=${plugin.id}")
            }
            plugins[plugin.id] = plugin
        }
    }

    fun get(id: Byte): BaseRemotingPluginKitGeneric? {
        return plugins[id]
    }

    companion object {
        val INSTANCE = RemotingPluginFactory()
    }
}

class PluginRegisterConflictError(val id: Byte, message: String) : Exception(message)