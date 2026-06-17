package dev.d4vid.mods.genesis.server

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.permissions.Permissions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files

private fun configComponent(message: String): Component {
    return genesisComponent(Component.literal("Config").withStyle(ChatFormatting.GOLD), Component.literal(message))
}

@Suppress("unused")
object Genesis : DedicatedServerModInitializer {
    const val MOD_ID = "genesis"
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitializeServer() {
        try {
            logger.info("Loading config...")
            loadConfig()
            logger.info("Successfully loaded config!")
        } catch (e: Exception) {
            logger.error("Failed to load config: ${e.stackTraceToString()}")
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, context, selection ->
            dispatcher.register(
                Commands.literal("genesis:reload")
                    .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR) }
                    .executes { context ->
                        try {
                            context.source.sendSystemMessage(configComponent("Reloading config..."))
                            loadConfig()
                            context.source.sendSuccess({ configComponent("Successfully reloaded config!") }, false)

                            0
                        } catch (e: Exception) {
                            context.source.sendFailure(configComponent("Failed to reload config: ${e.message}"))

                            1
                        }
                    })
        }
    }

    private fun loadConfig() {
        var file = File("./config/genesis.json")

        if (!file.exists()) {
            Files.createDirectories(file.parentFile.toPath())

            val stream = this.javaClass.getResourceAsStream("/config.json")!!
            Files.copy(stream, file.toPath())

            file = File("./config/genesis.json")
        }

        GenesisConfig.load(file.readText())
    }
}
