package dev.d4vid.mods.genesis.server.config.data.chat

import dev.d4vid.mods.genesis.server.config.field.ChatKeywordContent
import kotlinx.serialization.Serializable

@Serializable
data class ChatKeywordConfig(
    val base: String,
    val content: ChatKeywordContent,
    val requireOp: Boolean = false,
)
