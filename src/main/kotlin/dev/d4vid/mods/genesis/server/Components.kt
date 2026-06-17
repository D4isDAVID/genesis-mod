package dev.d4vid.mods.genesis.server

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

fun genesisComponent(context: Component, message: Component): Component {
    return Component.empty()
        .append(
            Component.literal("GenesisMod")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)
        )
        .append(Component.literal(" | "))
        .append(context)
        .append(
            Component.literal(" » ")
                .withStyle(ChatFormatting.RESET)
        )
        .append(message)
}
