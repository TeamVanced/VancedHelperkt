package core.wrapper.interaction

import core.util.randomColor
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
            color = randomColor
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
