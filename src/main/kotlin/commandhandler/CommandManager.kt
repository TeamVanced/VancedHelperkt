package commandhandler

import commands.BaseCommand
import commands.CommandType
import commands.`fun`.*
import commands.database.Settings
import commands.dev.CreateEmbed
import commands.dev.EmoteRole
import commands.moderation.*
import commands.quotes.*
import commands.utility.*
import commands.vanced.*
import database.modRoles
import database.owners
import database.prefix
import ext.sendMsg
import ext.sendStacktrace
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import java.awt.Color

class CommandManager {

    val commands = mutableListOf<ICommand>()
    val commandTypes = mutableListOf<CommandType>()

    fun addCommand(command: ICommand) {
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

    fun getCommand(commandName: String): ICommand? {
        return commands.firstOrNull { cmd ->
            cmd.commandName == commandName || cmd.commandAliases.contains(commandName)
        }
    }

    fun execute(event: GuildMessageReceivedEvent) {
        val inputText = event.message.contentRaw.removePrefix(event.guild.id.prefix).split("\\s+".toRegex())
        val command = getCommand(inputText[0].toLowerCase())

        if (command != null) {
            execWithChecks(command, event, inputText)
        }
    }

    fun execWithChecks(command: ICommand, event: GuildMessageReceivedEvent, args: List<String>) {
        val commandContext = CommandContext(event, args.subList(1, args.size) as MutableList<String>)

        val guildId = event.guild.id
        val modRoles = guildId.modRoles
        val owners = guildId.owners

        event.guild.retrieveMemberById(event.author.id).queue { member ->
            if ((command.devOnly && !owners.contains(member.id)) || (command.commandType == CommandType.Moderation && !member.roles.any {
                    modRoles.contains(
                        it.id
                    )
                })) {
                event.channel.sendMsg("You are not allowed to use this command!")
                return@queue
            }

            try {
                command.execute(commandContext)
            } catch (e: Exception) {
                event.channel.sendMsg("Sorry, something went wrong")
                EmbedBuilder().setColor(Color.red).sendStacktrace(event.guild, e.cause?.message, e.stackTraceToString())
            }
        }

    }

    fun onReactionAdd(event: MessageReactionAddEvent) {
        commands.forEach {
            if ((it as BaseCommand).messageId == event.messageId) {
                it.onReactionAdd(event)
                return
            }
        }
    }

    fun onReactionRemove(event: MessageReactionRemoveEvent) {
        commands.filter { it is IMessageReactionListener }.forEach {
            if ((it as BaseCommand).messageId == event.messageId) {
                it.onReactionRemove(event)
                return
            }
        }
    }

    init {
        arrayOf(
            Settings(),
            CreateEmbed(),
            EmoteRole(),
            Country(),
            EightBall(),
            Emote(),
            EmoteBoard(),
            F(),
            Gender(),
            How(),
            IQ(),
            PPSize(),
            Mute(),
            Purge(),
            Unmute(),
            Unwarn(),
            Warn(),
            Warns(),
            AddQuote(),
            AddStar(),
            GetQuote(this),
            Quote(this),
            RandomQuote(),
            RemoveQuote(),
            RemoveStar(),
            StarBoard(),
            Avatar(),
            BAT(),
            Colourme(),
            Help(this),
            Ping(),
            BugReport(),
            Features(),
            Info(),
            SupportUs(),
            Troubleshoot()
        ).forEach {
            addCommand(it)
        }
    }

}