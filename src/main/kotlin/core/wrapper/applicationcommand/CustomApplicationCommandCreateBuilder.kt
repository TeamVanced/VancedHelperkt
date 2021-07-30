package core.wrapper.applicationcommand

import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.interaction.ApplicationCommandCreateBuilder

@OptIn(KordPreview::class)
data class CustomApplicationCommandCreateBuilder(
    val arguments: ApplicationCommandCreateBuilder.() -> Unit = {},
)