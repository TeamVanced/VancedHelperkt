package core.wrapper.interaction

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.interaction.BaseInteractionResponseCreateBuilder
import dev.kord.rest.builder.interaction.embed
import dev.kord.rest.builder.message.EmbedBuilder

@OptIn(KordPreview::class)
data class CustomInteractionResponseCreateBuilder(
    val baseInteractionResponseCreateBuilder: BaseInteractionResponseCreateBuilder
) {
    fun embed(
        block: EmbedBuilder.() -> Unit
    ) {
        baseInteractionResponseCreateBuilder.embed {
            color = Color((Math.random() * 0x1000000).toInt())
            block()
        }
    }

    var content
        get() = baseInteractionResponseCreateBuilder.content
        set(value) {
            baseInteractionResponseCreateBuilder.content = value
        }

}
