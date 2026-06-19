package dev.d4vid.mods.genesis.server.resourcePack

import java.io.File
import java.util.*

object ResourcePackPlayerData {
    const val PATH = "genesis_pack_accepted.txt"

    private val acceptedPlayers = mutableSetOf<UUID>()

    fun load() {
        try {
            val file = File(PATH)
            if (!file.exists()) {
                return
            }

            for (line in file.readLines()) {
                if (line.isBlank()) {
                    continue
                }

                acceptedPlayers.add(UUID.fromString(line.trim()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            val file = File(PATH)
            val writer = file.writer()

            for (uuid in acceptedPlayers) {
                writer.appendLine(uuid.toString())
            }

            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun registerPlayerAccepted(uuid: UUID) {
        acceptedPlayers.add(uuid)
    }

    fun hasPlayerAccepted(uuid: UUID): Boolean {
        return acceptedPlayers.contains(uuid)
    }
}
