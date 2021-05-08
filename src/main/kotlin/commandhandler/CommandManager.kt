package commandhandler

import commands.base.BaseCommand
import database.modRoles
import database.owners
import database.prefix
import ext.sendMessageWithChecks
import ext.sendStacktrace
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import org.reflections.Reflections
import type.CommandType

class CommandManager {

    val commands = mutableListOf<BaseCommand>()
    val commandTypes = mutableListOf<CommandType>()

    private fun addCommand(command: BaseCommand) {
        if (commands.contains(command)) {
            println("Command already exists, skipping...")
            return
        }
        commands.add(command)
        val commandType = command.commandType
        if (!commandTypes.contains(commandType) && commandType != CommandType.Database && commandType != CommandType.Dev) {
            commandTypes.add(commandType)
        }

    }

    fun getCommand(commandName: String): BaseCommand? {
        return commands.firstOrNull { cmd ->
            cmd.commandName == commandName || cmd.commandAliases.contains(commandName)
        }
    }

    fun execute(event: GuildMessageReceivedEvent) {
        val inputText = event.message.contentRaw.removePrefix(event.guild.id.prefix).split("\\s+".toRegex())
        val command = getCommand(inputText[0].lowercase())

        if (command != null) {
            execWithChecks(command, event, inputText)
        }
    }

    fun execWithChecks(command: BaseCommand, event: GuildMessageReceivedEvent, args: List<String>) {
        val commandContext = CommandContext(event, args.subList(1, args.size) as MutableList<String>)

        val guildId = event.guild.id
        val modRoles = guildId.modRoles
        val owners = guildId.owners
        val guild = event.guild

        guild.retrieveMemberById(event.author.id).queue { member ->
            if (
                (command.devOnly && !owners.contains(member.id))
                || (command.commandType == CommandType.Moderation && !(member.roles.any { modRoles.contains(it.id) } && owners.contains(member.id)))
            ) {
                event.channel.sendMessageWithChecks("You are not allowed to use this command!")
                return@queue
            }

            try {
                command.execute(commandContext)
            } catch (e: Exception) {
                event.channel.sendMessageWithChecks("Sorry, something went wrong")
                guild.sendStacktrace(e.cause?.message, e.stackTraceToString())
            }
        }

    }

    fun onReactionAdd(event: MessageReactionAddEvent) {
        event.channel.retrieveMessageById(event.messageId).queue({ message ->
            commands.firstOrNull { it.commandMessage.containsKey(event.channel) && it.commandMessage.containsValue(message) }?.onReactionAdd(event)
            return@queue
        }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
    }

    fun onReactionRemove(event: MessageReactionRemoveEvent) {
        event.channel.retrieveMessageById(event.messageId).queue({ message ->
            commands.firstOrNull { it.commandMessage.containsKey(event.channel) && it.commandMessage.containsValue(message) }?.onReactionRemove(event)
            return@queue
        }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
    }

    init {
        Reflections("commands").getSubTypesOf(BaseCommand::class.java).forEach {
            try {
                addCommand(it.getDeclaredConstructor().newInstance())
            } catch (e: NoSuchMethodException) {}
        }
    }

}