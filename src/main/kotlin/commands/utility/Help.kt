package commands.utility

import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandType.Utility
import database.prefix
import ext.optional

class Help(private val commandManager: CommandManager) : BaseCommand(
    commandName = "help",
    commandDescription = "Get all available commands from bot",
    commandType = Utility,
    commandArguments = mapOf("command name".optional()),
    commandAliases = listOf("h")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args

        if (args.isNotEmpty()) {
            val command = commandManager.getCommand(args[0])
            if (command != null) {
                ctx.event.channel.sendMsg((command).getHelpEmbed())
            } else {
                ctx.event.channel.sendMsg("Command not found!")
            }
        } else {
            val commands = mutableMapOf<String, List<String>>()
            val prefix = guildId.prefix
            commandManager.commandTypes.forEach { commandType ->
                commands[commandType.name] =
                    commandManager.commands.filter { it.commandType == commandType }.sortedBy { it.commandName }
                        .map { "`$prefix${it.commandName}` - ${it.commandDescription}" }
            }
            val embed = embedBuilder.setTitle("Help Menu")
                .setDescription("For info on a specific command, type `${guildId.prefix}help [command name]`!")
            commands.forEach {
                embed.addField(
                    it.key,
                    commands[it.key]?.joinToString("\n"),
                    false
                )
            }
            ctx.event.channel.sendMsg(embed.build())
        }
    }

}