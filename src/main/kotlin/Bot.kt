import core.command.CommandManager
import core.database.settings
import core.listener.MessageListener
import core.listener.UserListener
import dev.kord.common.entity.AuditLogEvent
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.any
import dev.kord.core.behavior.getAuditLogEntries
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.SelectMenuInteraction
import dev.kord.core.event.guild.BanRemoveEvent
import dev.kord.core.event.guild.MemberLeaveEvent
import dev.kord.core.event.guild.MemberUpdateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class Bot : KoinComponent {

    private val commandManager: CommandManager by inject()
    private val messageListener: MessageListener by inject()
    private val userListener: UserListener by inject()

    private val logger = LoggerFactory.getLogger("Vanced Helper")

    @OptIn(PrivilegedIntent::class)
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

        kord.on<MemberUpdateEvent> {
            val boosterRoleSnowflake = Snowflake(settings.boosterRoleId)
            val oldWasBooster = old?.roles?.any { it.id == boosterRoleSnowflake }
                ?: return@on
            val newIsBooster = member.roles.any { it.id == boosterRoleSnowflake }

            when {
                !oldWasBooster && newIsBooster -> {}
                oldWasBooster && !newIsBooster -> {
                    userListener.onMemberUnboostGuild(member, logger)
                }
            }
        }

        kord.on<MemberLeaveEvent> {
            userListener.onMemberLeaveGuild(old ?: return@on, logger)
        }

        // Ban event
        kord.on<MemberLeaveEvent> {
            delay(5000L)

            val ban = guild.getAuditLogEntries {
                userId = user.id
                action = AuditLogEvent.MemberBanAdd
            }.firstOrNull() ?: return@on

            val mod = guild.getMember(ban.userId)
            val bannedUser = guild.getMember(ban.targetId!!)

            userListener.onMemberBan(mod, bannedUser, ban.reason)
        }

        // Kick event
        kord.on<MemberLeaveEvent> {
            delay(5000L)

            val kick = guild.getAuditLogEntries {
                userId = user.id
                action = AuditLogEvent.MemberKick
            }.firstOrNull() ?: return@on

            val mod = guild.getMember(kick.userId)
            val kickedUser = guild.getMember(kick.targetId!!)

            userListener.onMemberKick(mod, kickedUser, kick.reason)
        }

        kord.on<BanRemoveEvent> {
            userListener.onMemberUnban(user)
        }

        kord.login {
            intents = Intents(
                Intent.GuildBans,
                Intent.GuildMembers,
                Intent.GuildMessages,
                Intent.Guilds,
            )

            if (settings.logChannelId != 0L) {
                val channel = kord.getGuild(config.guildSnowflake)?.getChannel(Snowflake(settings.logChannelId))
                val messageChannel = channel as? MessageChannel ?: return

                messageChannel.createMessage("I just started!")
            }
        }
    }

}