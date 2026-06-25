package dev.d4vid.mods.genesis.server.config.data.pvp

import dev.d4vid.mods.genesis.server.config.serialization.NonNegativeDurationMinutesDoubleSerializer
import kotlinx.serialization.Serializable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.Team
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class PvpConfig(
    val protectHarmlessPets: Boolean = true,
    private val protectedTeams: Set<String> = setOf("peaceful"),
    private val spawnProtection: PvpSpawnProtectionConfig = PvpSpawnProtectionConfig(),
    @Serializable(with = NonNegativeDurationMinutesDoubleSerializer::class)
    val joinProtectionMinutes: Duration = 5.minutes,
    @Serializable(with = NonNegativeDurationMinutesDoubleSerializer::class)
    val respawnProtectionMinutes: Duration = 60.minutes,
    val detection: PvpDetectionConfig = PvpDetectionConfig(),
    val killPlayerOnCombatLog: Boolean = true,
    val damageMultipliers: PvpDamageMultipliersConfig = PvpDamageMultipliersConfig()
) {
    fun isTeamProtected(team: Team): Boolean {
        return protectedTeams.contains(team.name)
    }

    fun isSpawnProtected(player: ServerPlayer): Boolean {
        val dx = player.x - spawnProtection.x
        val dz = player.z - spawnProtection.z
        val radius = spawnProtection.radius

        return (dx * dx + dz * dz) <= (radius * radius)
    }
}
