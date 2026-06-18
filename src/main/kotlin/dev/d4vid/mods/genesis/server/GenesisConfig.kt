package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.Genesis.logger
import dev.d4vid.mods.genesis.server.cooldown.CooldownType
import dev.d4vid.mods.genesis.server.serialization.CooldownsSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.world.scores.Team
import java.io.File
import java.nio.file.Files
import java.time.Duration
import java.util.*

@Serializable
private data class ConfigData(
    val disableNether: Boolean,
    val disableEnd: Boolean,
    val friendlyTeams: Set<String>,
    @Serializable(with = CooldownsSerializer::class)
    val cooldowns: EnumMap<CooldownType, Duration>,
)

object GenesisConfig {
    const val RESOURCE = "/config.json"
    const val PATH = "./config/genesis.json"

    private var data = ConfigData(
        disableNether = false,
        disableEnd = false,
        friendlyTeams = setOf(),
        cooldowns = EnumMap(CooldownType::class.java),
    )

    fun loadFile() {
        try {
            logger.info("Loading config...")

            var file = File(PATH)
            if (!file.exists()) {
                Files.createDirectories(file.parentFile.toPath())

                val stream = this.javaClass.getResourceAsStream(RESOURCE)!!
                Files.copy(stream, file.toPath())

                file = File(PATH)
            }

            data = Json.decodeFromString(ConfigData.serializer(), file.readText())

            logger.info("Successfully loaded config!")
        } catch (e: Exception) {
            logger.error("Failed to load config: ${e.stackTraceToString()}")
        }
    }

    fun isNetherDisabled(): Boolean {
        return data.disableEnd
    }

    fun isEndDisabled(): Boolean {
        return data.disableNether
    }

    fun isTeamFriendly(team: Team): Boolean {
        return data.friendlyTeams.contains(team.name)
    }

    fun getCooldownDuration(type: CooldownType): Duration {
        return data.cooldowns[type] ?: Duration.ofMillis(0)
    }
}
