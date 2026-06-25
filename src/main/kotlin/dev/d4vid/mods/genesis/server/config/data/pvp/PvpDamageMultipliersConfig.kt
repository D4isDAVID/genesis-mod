package dev.d4vid.mods.genesis.server.config.data.pvp

import kotlinx.serialization.Serializable

@Serializable
data class PvpDamageMultipliersConfig(
    val minecartTntExplosion: Float = 1F,
    val endCrystalExplosion: Float = 1F,
    val respawnAnchorExplosion: Float = 1F,
)
