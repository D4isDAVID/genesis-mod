package dev.d4vid.mods.genesis.server.config.data.custom.item

import kotlinx.serialization.Serializable

@Serializable
data class HermesBootsConfig(
    val addArmor: Double = 3.0,
    val addToughness: Double = 2.0,
    val addSpeedMultiplier: Double = 0.25,
)
