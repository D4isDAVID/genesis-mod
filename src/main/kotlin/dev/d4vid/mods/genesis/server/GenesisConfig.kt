package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.cooldown.CooldownType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.time.Duration
import java.util.EnumMap

object GenesisConfig {
    private var cooldowns = EnumMap<CooldownType, Duration>(CooldownType::class.java)

    fun load(raw: String) {
        val json = Json.parseToJsonElement(raw).jsonObject
        val cooldowns = json.getValue("cooldowns").jsonObject

        cooldowns.entries.forEach { (key, value) ->
            val enum = CooldownType.fromKey(key)

            if (enum == null) {
                Genesis.logger.warn("Unknown cooldown key ${key}!")
                return@forEach
            }

            this.cooldowns[enum] = Duration.ofSeconds(value.jsonPrimitive.long)
        }
    }

    fun getCooldownDuration(type: CooldownType): Duration {
        return cooldowns[type]!!
    }
}
