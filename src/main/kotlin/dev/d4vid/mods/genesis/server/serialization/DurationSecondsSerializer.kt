package dev.d4vid.mods.genesis.server.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration

object DurationSecondsSerializer : KSerializer<Duration> {
    const val ONE_SECOND_MILLIS = 1000.0

    override val descriptor = PrimitiveSerialDescriptor("PositiveSecondsDuration", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: Duration) {
        val seconds = value.toMillis() / ONE_SECOND_MILLIS

        if (seconds < 0) {
            throw SerializationException("Duration can't be negative, got $value")
        }

        encoder.encodeDouble(seconds)
    }

    override fun deserialize(decoder: Decoder): Duration {
        val seconds = decoder.decodeDouble()
        val millis = seconds * ONE_SECOND_MILLIS

        if (millis < 0) {
            throw SerializationException("Duration can't be negative, got $seconds")
        }

        return Duration.ofMillis(millis.toLong())
    }
}
