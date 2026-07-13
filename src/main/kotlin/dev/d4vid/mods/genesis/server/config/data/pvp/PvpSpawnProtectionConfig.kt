package dev.d4vid.mods.genesis.server.config.data.pvp

import kotlinx.serialization.Serializable

@Serializable
data class PvpSpawnProtectionConfig(
    val radius: Double = 500.0,
    val x: Double = 155.0,
    val z: Double = 71.0,
)
