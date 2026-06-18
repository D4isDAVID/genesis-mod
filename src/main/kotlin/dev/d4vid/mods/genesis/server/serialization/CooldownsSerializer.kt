package dev.d4vid.mods.genesis.server.serialization

import dev.d4vid.mods.genesis.server.cooldown.CooldownType
import java.time.Duration

object CooldownsSerializer : EnumMapSerializer<CooldownType, Duration>(
    CooldownType::class.java,
    CooldownType.serializer(),
    DurationSecondsSerializer
)
