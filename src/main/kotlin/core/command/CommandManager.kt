package core.command

import config

import core.command.base.BaseCommand
import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.SubCommand
import dev.kord.core.entity.interaction.boolean
import dev.kord.rest.builder.interaction.BaseInteractionResponseCreateBuilder

class CommandManager {

    private val commands = mutableListOf<BaseCommand>()

    suspend fun addCommand(command: BaseCommand) {
        if (!commands.contains(command)){
            command.preInit()
            commands.add(command)
        }
    }

   fun getCommand(name: String) = commands.find { it.name == name }

    @OptIn(KordPreview::class)
    suspend fun respond(interaction: Interaction) {
        val commandInteraction = interaction as? CommandInteraction ?: return
        val command = commandInteraction.command

        val send = command.options["send"]?.boolean() ?: true
        val commandObject = getCommand(command.rootName)!!

        suspend fun BaseInteractionResponseCreateBuilder.execute() {
            val ctx = CommandContext(
                args = command.options,
                author = commandInteraction.user.asMember(Snowflake(config.guildId)),
                channel = commandInteraction.getChannel(),
                subCommand = command as? SubCommand,
                interactionResponseCreateBuilder = CustomInteractionResponseCreateBuilder(
                    baseInteractionResponseCreateBuilder = this
                )
            )
            commandObject.execute(ctx)
        }

        if (send) {
            interaction.respondPublic {
                execute()
            }
        } else {
            interaction.respondEphemeral {
                execute()
            }
        }
    }

}