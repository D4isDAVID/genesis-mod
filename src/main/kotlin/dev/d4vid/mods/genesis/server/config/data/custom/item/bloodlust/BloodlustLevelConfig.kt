package dev.d4vid.mods.genesis.server.config.data.custom.item.bloodlust

import kotlinx.serialization.Serializable

@Serializable
data class BloodlustLevelConfig(
    val requiredKills: Int,
    val sharpnessLevel: Int,
)
