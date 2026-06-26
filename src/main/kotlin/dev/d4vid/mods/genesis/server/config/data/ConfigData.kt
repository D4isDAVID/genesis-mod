package dev.d4vid.mods.genesis.server.config.data

import dev.d4vid.mods.genesis.server.config.data.chat.ChatConfig
import dev.d4vid.mods.genesis.server.config.data.cooldowns.CooldownsConfig
import dev.d4vid.mods.genesis.server.config.data.custom.CustomConfig
import dev.d4vid.mods.genesis.server.config.data.items.ItemsConfig
import dev.d4vid.mods.genesis.server.config.data.pvp.PvpConfig
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val custom: CustomConfig = CustomConfig(),
    val portals: PortalsConfig = PortalsConfig(),
    val blocks: BlocksConfig = BlocksConfig(),
    val recipes: RecipesConfig = RecipesConfig(),
    val spoofClientPackets: SpoofClientPacketsConfig = SpoofClientPacketsConfig(),
    val cooldowns: CooldownsConfig = CooldownsConfig(),
    val pvp: PvpConfig = PvpConfig(),
    val items: ItemsConfig = ItemsConfig(),
    val chat: ChatConfig = ChatConfig(),
)
