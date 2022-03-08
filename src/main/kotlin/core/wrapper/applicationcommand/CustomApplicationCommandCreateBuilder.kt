package core.wrapper.applicationcommand

import dev.kord.rest.builder.interaction.ChatInputCreateBuilder

class CustomApplicationCommandCreateBuilder(
    val arguments: ChatInputCreateBuilder.() -> Unit = {},
)