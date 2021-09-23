package core.wrapper.applicationcommand

import dev.kord.rest.builder.interaction.ChatInputCreateBuilder


data class CustomApplicationCommandCreateBuilder(
    val arguments: ChatInputCreateBuilder.() -> Unit = {},
)