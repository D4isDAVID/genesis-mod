package dev.d4vid.mods.genesis.server.config.data.custom.item

import kotlinx.serialization.Serializable

@Serializable
data class HermesBootsConfig(
    val addSpeedMultiplier: Double = 0.1,
)
