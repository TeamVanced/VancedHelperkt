package core.command

import config
import core.command.base.BaseCommand
import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.CommandGroup
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.SubCommand

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

        val commandObject = getCommand(command.rootName)!!

        interaction.respondPublic {
            commandObject.execute(
                ctx = CommandContext(
                    args = command.options,
                    author = commandInteraction.user.asMember(Snowflake(config.guildId)),
                    channel = commandInteraction.getChannel(),
                    subCommand = command as? SubCommand,
                    subCommandGroup = command as? GroupCommand,
                    interactionResponseCreateBuilder = CustomInteractionResponseCreateBuilder(
                        baseInteractionResponseCreateBuilder = this
                    )
                )
            )
        }
    }

}