package core.command

import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.entity.interaction.OptionValue
import dev.kord.core.entity.interaction.SubCommand

@OptIn(KordPreview::class)
data class CommandContext(
    val author: Member,
    val channel: Channel,
    val args: Map<String, OptionValue<*>>,
    val subCommand: SubCommand?,
    val subCommandGroup: GroupCommand?,
    private val interactionResponseCreateBuilder: CustomInteractionResponseCreateBuilder,
    private val commandInteraction: CommandInteraction,
) {
    fun respond(
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        interactionResponseCreateBuilder.block()
    }
}