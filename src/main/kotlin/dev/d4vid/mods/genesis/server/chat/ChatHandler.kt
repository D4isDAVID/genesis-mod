package dev.d4vid.mods.genesis.server.chat

import dev.d4vid.mods.genesis.server.config.GenesisConfig
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent

class ChatHandler(private val config: GenesisConfig) {
    private inline val chatConfig
        get() = config.data.chat

    fun initialize() {
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE) { player, component ->
            if (player == null) {
                return@register component
            }

            ChatKeywordHandler(chatConfig, player).apply(
                ChatFormatHandler(chatConfig, player).apply(component)
            )
        }
    }
}
