package dev.d4vid.mods.genesis.server.config.data

import kotlinx.serialization.Serializable

@Serializable
data class SpoofClientPacketsConfig(
    val disableEntityHealthUpdates: Boolean = true,
)
