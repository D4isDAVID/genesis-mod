package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.syncher.SyncedDataHolder

object GenesisPacketSpoofEvents {
    val ALLOW_ENTITY_HEALTH_SYNC = EventFactory.createArrayBacked(AllowEntityHealthSync::class.java) { listeners ->
        AllowEntityHealthSync { entity ->
            for (listener in listeners) {
                val result = listener.allowEntityHealthSync(entity)

                if (!result) {
                    return@AllowEntityHealthSync false
                }
            }

            true
        }
    }

    fun interface AllowEntityHealthSync {
        fun allowEntityHealthSync(entity: SyncedDataHolder): Boolean
    }
}
