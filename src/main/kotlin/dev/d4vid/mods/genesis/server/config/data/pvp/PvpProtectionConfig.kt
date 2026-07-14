package dev.d4vid.mods.genesis.server.config.data.pvp

import dev.d4vid.mods.genesis.server.config.serialization.NonNegativeDurationMinutesDoubleSerializer
import kotlinx.serialization.Serializable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.Team
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class PvpProtectionConfig(
    val protectHarmlessPets: Boolean = true,
    private val protectedTeams: Set<String> = setOf(),
    private val spawnProtection: PvpSpawnProtectionConfig = PvpSpawnProtectionConfig(),
    @Serializable(with = NonNegativeDurationMinutesDoubleSerializer::class)
    val newPlayerProtectionMinutes: Duration = 60.minutes,
    @Serializable(with = NonNegativeDurationMinutesDoubleSerializer::class)
    val respawnProtectionMinutes: Duration = 60.minutes,
) {
    fun isTeamProtected(team: Team): Boolean {
        return protectedTeams.contains(team.name)
    }

    fun isSpawnProtected(player: ServerPlayer): Boolean {
        return player.x in spawnProtection.rangeX && player.z in spawnProtection.rangeZ
    }
}
