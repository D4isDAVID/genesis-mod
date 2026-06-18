package dev.d4vid.mods.genesis.server.cooldown

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CooldownType {
    @SerialName("spear_lunge")
    Lunge,
}
