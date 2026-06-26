package dev.d4vid.mods.genesis.server.chat

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.chat.ChatConfig
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent

class ChatHandler {
    private lateinit var config: ChatConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.chat }

        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE) { player, component ->
            if (player == null) {
                return@register component
            }

            ChatKeywordHandler(config, player).apply(
                ChatFormatHandler(config, player).apply(component)
            )
        }
    }
}
