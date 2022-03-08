package core.command

import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.OptionValue
import dev.kord.core.entity.interaction.SubCommand
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow

class CommandContext(
    val author: Member,
    val channel: Channel,
    val args: Map<String, OptionValue<*>>,
    val subCommand: SubCommand?,
    val subCommandGroup: GroupCommand?,
    val guild: Guild,
    val kord: Kord,
    private val commandInteraction: GuildChatInputCommandInteraction,
    private val commandName: String,
) {
    suspend fun respondPublic(
        deleteButton: Boolean = false,
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        commandInteraction.respondPublic {
            respond(deleteButton, block)
        }
    }

    suspend fun respondEphemeral(
        block: CustomInteractionResponseCreateBuilder.() -> Unit
    ) {
        commandInteraction.respondEphemeral {
            respond(deleteButton = false, block)
        }
    }

    private fun MessageCreateBuilder.respond(
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
