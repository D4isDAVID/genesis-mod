package dev.d4vid.mods.genesis.server.config.data.pvp

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PvpSpawnProtectionConfig(
    private val radius: Double = 700.0,
    private val x: Double = 155.0,
    private val z: Double = 71.0,
) {
    @Transient
    val rangeX = (x - radius / 2)..(x + radius / 2)

    @Transient
    val rangeZ = (z - radius / 2)..(z + radius / 2)
}
