package core.wrapper.interaction

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.PersistentMessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed

@OptIn(KordPreview::class)
data class CustomInteractionResponseCreateBuilder(
    val baseInteractionResponseCreateBuilder: PersistentMessageCreateBuilder
) {
    fun embed(
        block: EmbedBuilder.() -> Unit
    ) {
        baseInteractionResponseCreateBuilder.embed {
            color = Color((Math.random() * 0x1000000).toInt())
            block()
        }
    }

    fun actionRow(
        block: ActionRowBuilder.() -> Unit
    ) {
        baseInteractionResponseCreateBuilder.actionRow(block)
    }

    var content
        get() = baseInteractionResponseCreateBuilder.content
        set(value) {
            baseInteractionResponseCreateBuilder.content = value
        }

}
