package dev.d4vid.mods.genesis.server.cooldown

enum class CooldownType(val key: String) {
    Lunge("spear_lunge");

    companion object {
        private val keyMap = entries.associateBy { it.key }

        fun fromKey(key: String): CooldownType? {
            return keyMap[key]
        }
    }
}
