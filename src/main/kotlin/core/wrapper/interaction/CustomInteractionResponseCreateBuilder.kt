package core.wrapper.interaction

import core.util.randomColor
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed

data class CustomInteractionResponseCreateBuilder(
    val persistentMessageCreateBuilder: MessageCreateBuilder
) {
    fun embed(
        block: EmbedBuilder.() -> Unit
    ) {
        persistentMessageCreateBuilder.embed {
            color = randomColor
            block()
        }
    }

    fun actionRow(
        block: ActionRowBuilder.() -> Unit
    ) {
        persistentMessageCreateBuilder.actionRow(block)
    }

    var content
        get() = persistentMessageCreateBuilder.content
        set(value) {
            persistentMessageCreateBuilder.content = value
        }

}
