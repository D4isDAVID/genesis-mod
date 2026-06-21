package dev.d4vid.mods.genesis.server.resourcePack

import dev.d4vid.mods.genesis.server.GenesisConfig
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket
import net.minecraft.server.level.ServerPlayer
import java.util.*

fun registerResourcePackLoader() {
    ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
        sendResourcePack(handler.player)
    }
}

private fun sendResourcePack(player: ServerPlayer) {

    val url = GenesisConfig.getResourcePackUrl()
    val hash = GenesisConfig.getResourcePackSha1()
    val prompt = GenesisConfig.getResourcePackPrompt()

    player.connection.send(
        ClientboundResourcePackPushPacket(
            UUID.randomUUID(),
            url,
            hash,
            false,
            Optional.of(Component.literal(prompt))
        )
    )
}
