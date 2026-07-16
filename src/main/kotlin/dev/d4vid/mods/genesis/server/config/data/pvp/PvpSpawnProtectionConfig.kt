package dev.d4vid.mods.genesis.server.config.data.pvp

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PvpSpawnProtectionConfig(
    private val radius: Double = 700.0,
    private val x: Double = 0.0,
    private val z: Double = 0.0,
) {
    @Transient
    val rangeX = (x - radius)..(x + radius)

    @Transient
    val rangeZ = (z - radius)..(z + radius)
}
