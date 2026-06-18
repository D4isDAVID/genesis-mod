package dev.d4vid.mods.genesis.server.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

open class EnumMapSerializer<K : Enum<K>, V>(
    private val enumClass: Class<K>,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
) : KSerializer<EnumMap<K, V>> {
    private val mapSerializer = MapSerializer(keySerializer, valueSerializer)

    override val descriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: EnumMap<K, V>) {
        mapSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): EnumMap<K, V> {
        val map = mapSerializer.deserialize(decoder)

        return EnumMap<K, V>(enumClass).apply { putAll(map) }
    }
}
