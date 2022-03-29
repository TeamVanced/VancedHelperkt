package core.command

import config
import core.command.base.BaseCommand
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.application.GuildApplicationCommand
import dev.kord.core.entity.interaction.*
import org.reflections.Reflections
import org.slf4j.Logger
import java.lang.reflect.Modifier

class CommandManager {

    private val commands = mutableListOf<BaseCommand>()

    private fun addCommand(command: BaseCommand) {
        if (!commands.contains(command)) {
            commands.add(command)
        }
    }

    suspend fun runPreInit() {
        commands.forEach {
            it.preInit()
        }
    }

    fun addCommands() {
        val commands = Reflections("commands")
            .getSubTypesOf(BaseCommand::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) }
            .map { it.getConstructor().newInstance() }
            .sortedBy { it.commandName }

        commands.forEach {
            addCommand(it)
        }
    }

    suspend fun registerCommands(kord: Kord, logger: Logger) {
        commands.forEach { command ->
            with(command) {
                logger.info("Registering a slash command: $commandName")

                val registeredCommand = kord.createGuildChatInputCommand(
                    guildId = Snowflake(config.guildId),
                    name = commandName,
                    description = commandDescription,
                    builder = {
                        defaultPermission = defaultPermissions
                        commandOptions().arguments(this)
                    }
                )
                command.commandId = registeredCommand.id
            }
        }
    }

    suspend fun unregisterCommands(kord: Kord, logger: Logger) {
        logger.info("Unregistering all slash commands...")
        kord.getGuildApplicationCommands(config.guildSnowflake)
            .collect {
                it.delete()
            }
    }

    suspend fun configureCommandPermissions(
        kord: Kord,
        logger: Logger,
    ) {
        logger.info("Configuring command permissions...")

        //second method
        val commands = mutableListOf<GuildApplicationCommand>()
        kord.getGuildApplicationCommands(config.guildSnowflake).collect { commands.add(it) }
        kord.bulkEditApplicationCommandPermissions(config.guildSnowflake) {
            for (command in commands) {
                command(command.id) {
                    val botCommand = getCommand(command.name)

                    botCommand?.commandPermissions()?.permissions?.invoke(this)
                }
            }
        }
    }

    private fun getCommand(name: String) = commands.find { it.commandName == name }

    suspend fun respondCommandInteraction(interaction: GuildChatInputCommandInteraction) {
        val command = interaction.command

        val commandObject = getCommand(command.rootName)!!

        commandObject.execute(
            ctx = CommandContext(
                args = CommandArguments(
                    strings = command.strings,
                    integers = command.integers,
                    numbers = command.numbers,
                    booleans = command.booleans,
                    users = command.users,
                    members = command.members,
                    channels = command.channels,
                    roles = command.roles,
                    mentionables = command.mentionables,
                    attachments = command.attachments
                ),
                author = interaction.user.asMember(Snowflake(config.guildId)),
                channel = interaction.getChannel(),
                guild = interaction.getGuild(),
                kord = interaction.kord,
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