package core.command

import config
import core.command.base.BaseCommand
import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.entity.interaction.SelectMenuInteraction
import dev.kord.core.entity.interaction.SubCommand

@OptIn(KordPreview::class)
class CommandManager {

    private val commands = mutableListOf<BaseCommand>()

    suspend fun addCommand(command: BaseCommand) {
        if (!commands.contains(command)){
            command.preInit()
            commands.add(command)
        }
    }

    fun getCommand(name: String) = commands.find { it.commandName == name }

    suspend fun respond(interaction: CommandInteraction) {
        val command = interaction.command

        val commandObject = getCommand(command.rootName)!!

        interaction.respondPublic {
            commandObject.execute(
                ctx = CommandContext(
                    args = command.options,
                    author = interaction.user.asMember(Snowflake(config.guildId)),
                    channel = interaction.getChannel(),
                    subCommand = command as? SubCommand,
                    subCommandGroup = command as? GroupCommand,
                    interactionResponseCreateBuilder = CustomInteractionResponseCreateBuilder(
                        baseInteractionResponseCreateBuilder = this
                    ),
                    commandInteraction = interaction
                )
            )
        }
    }

    suspend fun respondSelectMenu(interaction: SelectMenuInteraction) {
        val commandName = interaction.componentId.substringBefore("-")
        val commandObject = getCommand(commandName)!!

        commandObject.onSelectMenu(interaction)

    }

}