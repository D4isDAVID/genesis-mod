package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.cooldown.CooldownType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.Duration
import java.util.*

object GenesisConfig {
    private var cooldowns = EnumMap<CooldownType, Duration>(CooldownType::class.java)

    fun load(raw: String) {
        val json = Json.parseToJsonElement(raw).jsonObject

        cooldowns.clear()
        json.getValue("cooldowns").jsonObject.entries.forEach { (key, valueElement) ->
            val enum = CooldownType.fromKey(key)
            val value = valueElement.jsonPrimitive.double

            if (enum == null) {
                Genesis.logger.warn("Unknown cooldown key $key!")
                return@forEach
            }

            if (value < 0) {
                Genesis.logger.warn("Cooldown for $key cannot be negative!")
                return@forEach
            }

            cooldowns[enum] = Duration.ofMillis((value * 1000).toLong())
        }
    }

    fun getCooldownDuration(type: CooldownType): Duration {
        return cooldowns[type] ?: Duration.ofMillis(0)
    }
}
