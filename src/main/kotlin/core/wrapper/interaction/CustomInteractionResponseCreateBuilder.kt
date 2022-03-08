package core.wrapper.interaction

import core.util.randomColor
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import java.io.InputStream

class CustomInteractionResponseCreateBuilder(
    private val persistentMessageCreateBuilder: MessageCreateBuilder
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

    fun addFile(name: String, inputStream: InputStream) {
        persistentMessageCreateBuilder.addFile(name, inputStream)
    }

    var content
        get() = persistentMessageCreateBuilder.content
        set(value) {
            persistentMessageCreateBuilder.content = value
        }

}
