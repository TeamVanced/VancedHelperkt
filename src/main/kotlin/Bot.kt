import core.command.CommandManager
import core.command.base.BaseCommand
import database.settings
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.reflections.Reflections
import org.slf4j.LoggerFactory

class Bot : KoinComponent {

    private val commandManager: CommandManager by inject()

    private val logger = LoggerFactory.getLogger("Vanced Helper")

    @OptIn(KordPreview::class)
    suspend fun start() {
        val kord = Kord(config.token)

        val commands = Reflections("commands")
            .getSubTypesOf(BaseCommand::class.java)
            .map { it.getConstructor().newInstance() }

        commands.forEach { command ->
            commandManager.addCommand(command)
            with(command) {
                logger.info("Registering command: ${command.name}")
                kord.registerCommand()
            }
        }

        kord.on<InteractionCreateEvent> {
            commandManager.respond(interaction)
        }

        kord.login {
            if (settings.logChannelId == 0L) {
                return@login
            }

            val channel = kord.getGuild(config.guildSnowflake)?.getChannel(Snowflake(settings.logChannelId))

            val messageChannel = channel as? MessageChannel ?: return

            messageChannel.createEmbed {
                val guild = channel.guild.asGuild()
                title = "I just started!"
                description = """
                    **Channels**: ${guild.channelIds.size}
                    **Members**:  ${guild.memberCount}
                """.trimIndent()
            }
        }
    }

}