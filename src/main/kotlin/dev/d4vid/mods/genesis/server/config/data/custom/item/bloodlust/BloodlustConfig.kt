package dev.d4vid.mods.genesis.server.config.data.custom.item.bloodlust

import kotlinx.serialization.Serializable

@Serializable
data class BloodlustConfig(
    val initialSharpnessLevel: Int = 2,
    val levels: List<BloodlustLevelConfig> = listOf(
        BloodlustLevelConfig(requiredKills = 1, sharpnessLevel = 3),
        BloodlustLevelConfig(requiredKills = 3, sharpnessLevel = 4),
        BloodlustLevelConfig(requiredKills = 5, sharpnessLevel = 5),
        BloodlustLevelConfig(requiredKills = 9, sharpnessLevel = 6),
    ),
)
