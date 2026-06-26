package dev.d4vid.mods.genesis.server.config

import dev.d4vid.mods.genesis.server.config.data.ConfigData
import net.fabricmc.fabric.api.event.EventFactory

fun interface GenesisConfigLoadCallback {
    companion object {
        val EVENT = EventFactory.createArrayBacked(GenesisConfigLoadCallback::class.java) { listeners ->
            GenesisConfigLoadCallback { data ->
                for (listener in listeners) {
                    listener.load(data)
                }
            }
        }
    }

    fun load(data: ConfigData)
}
