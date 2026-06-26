package dev.d4vid.mods.genesis.server.config.data.pvp

import dev.d4vid.mods.genesis.server.config.serialization.IdentifierSerializer
import kotlinx.serialization.Serializable
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.alchemy.Potion

@Serializable
data class PvpConfig(
    val protection: PvpProtectionConfig = PvpProtectionConfig(),
    val detection: PvpDetectionConfig = PvpDetectionConfig(),
    val damageMultipliers: PvpDamageMultipliersConfig = PvpDamageMultipliersConfig(),
    private val disableTippedArrows: List<@Serializable(with = IdentifierSerializer::class) Identifier> = listOf(
        Identifier.withDefaultNamespace("weakness"),
        Identifier.withDefaultNamespace("long_weakness"),
        Identifier.withDefaultNamespace("slow_falling"),
        Identifier.withDefaultNamespace("long_slow_falling"),
        Identifier.withDefaultNamespace("turtle_master"),
        Identifier.withDefaultNamespace("long_turtle_master"),
        Identifier.withDefaultNamespace("strong_turtle_master"),
    ),
) {
    fun isArrowPotionDisabled(potion: Holder<Potion>): Boolean {
        return disableTippedArrows.any { potion.`is`(it) }
    }
}
