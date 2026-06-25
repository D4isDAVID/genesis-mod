package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory

object GenesisPacketSpoofEvents {
    val ALLOW_ENTITY_HEALTH_SYNC = EventFactory.createArrayBacked(AllowEntityHealthSync::class.java) { listeners ->
        AllowEntityHealthSync {
            for (listener in listeners) {
                val result = listener.allowEntityHealthSync()

                if (!result) {
                    return@AllowEntityHealthSync false
                }
            }

            true
        }
    }

    fun interface AllowEntityHealthSync {
        fun allowEntityHealthSync(): Boolean
    }
}
