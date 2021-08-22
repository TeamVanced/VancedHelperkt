package core.command

import config
import core.command.base.BaseCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.*

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

    suspend fun respondCommandInteraction(interaction: CommandInteraction) {
        val command = interaction.command

        val commandObject = getCommand(command.rootName)!!

        commandObject.execute(
            ctx = CommandContext(
                args = command.options,
                author = interaction.user.asMember(Snowflake(config.guildId)),
                channel = interaction.getChannel(),
                subCommand = command as? SubCommand,
                subCommandGroup = command as? GroupCommand,
                commandInteraction = interaction,
                commandName = commandObject.commandName
            )
        )
    }

    suspend fun respondSelectMenuInteraction(interaction: SelectMenuInteraction) {
        val commandName = interaction.componentId.substringBefore("-")
        val commandObject = getCommand(commandName)!!

        commandObject.onSelectMenuInteraction(interaction)
    }

    suspend fun respondButtonInteraction(interaction: ButtonInteraction) {
        val commandName = interaction.componentId.substringBefore("-")
        val commandObject = getCommand(commandName)!!

        commandObject.onButtonInteraction(interaction)
    }

}