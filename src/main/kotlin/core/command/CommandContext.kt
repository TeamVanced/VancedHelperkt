package core.command

import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.interaction.OptionValue
import dev.kord.core.entity.interaction.SubCommand

@OptIn(KordPreview::class)
data class CommandContext(
    val author: Member,
    val channel: Channel,
    val args: Map<String, OptionValue<*>>,
    val subCommand: SubCommand?,
    private val interactionResponseCreateBuilder: CustomInteractionResponseCreateBuilder,
) {
    fun respond(
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        interactionResponseCreateBuilder.block()
    }
}
