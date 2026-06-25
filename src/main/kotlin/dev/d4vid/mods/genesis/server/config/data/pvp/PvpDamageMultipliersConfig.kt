package dev.d4vid.mods.genesis.server.config.data.pvp

import kotlinx.serialization.Serializable

@Serializable
data class PvpDamageMultipliersConfig(
    val minecartTntExplosion: Float = 0.8F,
    val endCrystalExplosion: Float = 0.4F,
    val respawnAnchorExplosion: Float = 0.8F,
)
