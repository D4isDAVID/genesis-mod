package dev.d4vid.mods.genesis.server.config.data.custom.item

import dev.d4vid.mods.genesis.server.config.data.custom.item.bloodlust.BloodlustConfig
import kotlinx.serialization.Serializable

@Serializable
data class CustomItemsConfig(
    val bloodlust: BloodlustConfig = BloodlustConfig(),
    val hermesBoots: HermesBootsConfig = HermesBootsConfig(),
)
