package core.command

import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.entity.interaction.OptionValue
import dev.kord.core.entity.interaction.SubCommand
import dev.kord.rest.builder.message.create.PersistentMessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow


data class CommandContext(
    val author: Member,
    val channel: Channel,
    val args: Map<String, OptionValue<*>>,
    val subCommand: SubCommand?,
    val subCommandGroup: GroupCommand?,
    private val commandInteraction: CommandInteraction,
    private val commandName: String,
) {
    suspend fun respondPublic(
        deleteButton: Boolean = true,
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        commandInteraction.respondPublic {
            respond(deleteButton, block)
        }
    }

    suspend fun respondEphemeral(
        deleteButton: Boolean = true,
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        commandInteraction.respondPublic {
            respond(deleteButton, block)
        }
    }

    private fun PersistentMessageCreateBuilder.respond(
        deleteButton: Boolean,
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        CustomInteractionResponseCreateBuilder(this).block()
        if (deleteButton) {
            actionRow {
                interactionButton(
                    style = ButtonStyle.Danger,
                    customId = "${commandName}-delete",
                    builder = {
                        label = "Delete"
                    }
                )
            }
        }
    }
}
