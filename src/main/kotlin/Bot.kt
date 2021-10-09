import core.command.CommandManager
import core.listener.MessageListener
import core.listener.ReactionListener
import core.database.settings
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.SelectMenuInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.event.message.ReactionRemoveEvent
import dev.kord.core.on
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class Bot : KoinComponent {

    private val commandManager: CommandManager by inject()
    private val messageListener: MessageListener by inject()
    private val reactionListener: ReactionListener by inject()

    private val logger = LoggerFactory.getLogger("Vanced Helper")


    suspend fun start() {
        val kord = Kord(config.token)

        with(commandManager) {
            addCommands()
            runPreInit()
        }

        kord.on<InteractionCreateEvent> {
            when (interaction) {
                is GuildChatInputCommandInteraction -> commandManager.respondCommandInteraction(interaction as GuildChatInputCommandInteraction)
                is SelectMenuInteraction -> commandManager.respondSelectMenuInteraction(interaction as SelectMenuInteraction)
                is ButtonInteraction -> commandManager.respondButtonInteraction(interaction as ButtonInteraction)
                else -> return@on
            }
        }

        kord.on<MessageCreateEvent> {
            with(messageListener) {
                filterMessageSpam(message)
                filterSingleMessageEmoteSpam(message)
                runDevCommands(message, commandManager, kord, logger)
            }
        }

        kord.on<ReactionAddEvent> {
            if (emoji is ReactionEmoji.Custom) {
                reactionListener.grantEmoteRole(
                    emoji = emoji as ReactionEmoji.Custom,
                    message = getMessage()
                )
            }
        }

        kord.on<ReactionRemoveEvent> {
            if (emoji is ReactionEmoji.Custom) {
                reactionListener.removeEmoteRole(
                    emoji = emoji as ReactionEmoji.Custom,
                    message = getMessage()
                )
            }
        }

        kord.login {
            if (settings.logChannelId == 0UL) {
                return@login
            }

            val channel = kord.getGuild(config.guildSnowflake)?.getChannel(Snowflake(settings.logChannelId))
            val messageChannel = channel as? MessageChannel ?: return

            messageChannel.createMessage("I just started!")
        }
    }

}