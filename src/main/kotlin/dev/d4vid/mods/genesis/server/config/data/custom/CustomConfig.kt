package dev.d4vid.mods.genesis.server.config.data.custom

import dev.d4vid.mods.genesis.server.config.data.custom.item.CustomItemsConfig
import kotlinx.serialization.Serializable

@Serializable
data class CustomConfig(
    val items: CustomItemsConfig = CustomItemsConfig(),
)
