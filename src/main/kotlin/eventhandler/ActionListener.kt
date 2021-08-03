package eventhandler

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import database.*
import database.collections.Emote
import ext.*
import jda
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.audit.TargetType
import net.dv8tion.jda.api.entities.MessageType
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.ErrorResponse
import org.litote.kmongo.findOne
import utils.stinks
import utils.stonks
import java.util.concurrent.TimeUnit

class ActionListener : ListenerAdapter() {

    private val emoteRegex = "<?(a)?:?(\\w{2,32}):(\\d{17,19})>?".toRegex()

    override fun onReady(event: ReadyEvent) {
        event.jda.guilds.forEach { guild ->
            val logChannel = guild.id.logChannel
            val guildId = guild.id
            if (logChannel.isNotEmpty()) {
                event.jda.getTextChannelById(logChannel)?.sendMessage(
                    EmbedBuilder().apply {
                        setTitle("I just started!")
                        setDescription(
                            "**Prefix**: `${guildId.prefix}`\n" +
                            "**Guilds**: `${event.jda.guilds.size}`\n" +
                            "**Channels**: `${guild.channels.size}\n`" +
                            "**Members**: `${guild.memberCount}`"
                        )
                    }.build()
                )?.queue()
            }
            val filter = BasicDBObject().append("guildId", guildId)
            guild.emotes.filter { !it.isAnimated }.forEach {
                val emote = "<:${it.name}:${it.id}>"
                if (emotesCollection.findOne(filter.append("emote", emote)) == null) {
                    emotesCollection.insertOne(
                        Emote(
                            guildId = guildId,
                            emote = emote,
                            usedCount = 0
                        )
                    )
                }
            }
        }
    }

    override fun onEmoteUpdateName(event: EmoteUpdateNameEvent) {
        if (event.emote.isAnimated)
            return

        val oldEmote = "<:${event.oldName}:${event.emote.id}>"
        val newEmote = "<:${event.newName}:${event.emote.id}>"
        val guildId = event.guild.id
        val filter = BasicDBObject().append("guildId", guildId).append("emote", oldEmote)
        if (emotesCollection.findOne(filter) == null) {
            emotesCollection.insertOne(
                Emote(
                    guildId = guildId,
                    emote = newEmote,
                    usedCount = 0
                )
            )
        } else {
            emotesCollection.updateOne(filter, Updates.set("emote", newEmote))
        }
    }

    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        val emoteRole = event.guild.id.getEmoteRoles(event.messageId, "<:${event.reactionEmote.asReactionCode}>")
        if (emoteRole != null) {
            val role = event.guild.getRoleById(emoteRole.roleId)
            if (role != null) {
                event.guild.addRoleToMember(event.member, role).queue()
            }

        }
    }

    override fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {
        val emoteRole = event.guild.id.getEmoteRoles(event.messageId, "<:${event.reactionEmote.asReactionCode}>")
        if (emoteRole != null) {
            val role = event.guild.getRoleById(emoteRole.roleId)
            val member = event.member
            if (role != null && member != null) {
                event.guild.removeRoleFromMember(member, role).queue()
            }
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message = event.message
        val messageContent = message.contentRaw
        val guildId = event.guild.id
        val channel = event.channel
        val member = event.member
        val words = messageContent.replace(".", "").replace(",", "").replace("\n", " ").replace("\\s+".toRegex(), " ").split(" ")
        val isSpamChannel = with(event.channel) { idLong == 361807727531393026 || idLong == 658364415439142913 }

        if (!isSpamChannel) {
            var duplicateCount = 0
            channel.history.retrievePast(10).queue { messages ->
                val memberMessages = messages.filter {
                    it.type != MessageType.GUILD_MEMBER_JOIN && !it.isWebhookMessage && it.author == event.author && it.contentRaw.equals(
                        messageContent,
                        ignoreCase = true
                    )
                }.take(5)
                if (memberMessages.size < 5 || event.author.isBot || (member != null && member.isMod(guildId))) {
                    return@queue
                }

                channel.deleteMessages(memberMessages).queue({
                    jda?.selfUser?.let { it1 -> event.member?.warn(it1, guildId, "Message spam", channel) }
                    channel.sendMessageWithChecks("${event.member?.asMention} has been warned for spamming messages")
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})

                return@queue
            }

            for (i in 1 until words.size) {
                if (words[i].equals(words[i - 1], ignoreCase = true) && words[i] !null) {
                    duplicateCount++
                } else {
                    duplicateCount = 0
                }
            }

            if (duplicateCount >= 5) {
                if (!event.author.isBot && member != null && !member.isMod(guildId)) {
                    message.delete().queue({
                        jda?.selfUser?.let { it1 -> member.warn(it1, guildId, "Message spam", channel) }
                        channel.sendMessageWithChecks("${member.asMention} has been warned for spamming messages")
                    }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
                }
            }
        }

        if (messageContent.contains(emoteRegex)) {
            val emotes = emoteRegex.findAll(messageContent)
            if (!isSpamChannel) {
                if (emotes.count() > 6) {
                    if (member != null) {
                        if (!member.isMod(guildId) && !event.author.isBot) {
                            message.delete().queue({
                                jda?.selfUser?.let { it1 -> member.warn(it1, guildId, "Emote spam", channel) }
                                channel.sendMessageWithChecks("${member.asMention} has been warned for spamming emotes")
                            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
                            return
                        }
                    }
                }
            }
            val filter = BasicDBObject().append("guildId", guildId)
            emotes.forEach { emote ->
                val id = emote.value.substringAfterLast(":").dropLast(1)
                if (!event.guild.emotes.map { it.id }.contains(id)) {
                    return@forEach
                }
                val emoteFilter = filter.append("emote", emote.value)
                val emoteCollection = emotesCollection.findOne(emoteFilter)
                if (emoteCollection == null) {
                    emotesCollection.insertOne(
                        Emote(
                            guildId = guildId,
                            emote = emote.value,
                            usedCount = 1
                        )
                    )
                } else {
                    emotesCollection.updateOne(emoteFilter, Updates.set("usedCount", emoteCollection.usedCount + 1))
                }
            }
        }
    }

    override fun onEmoteAdded(event: EmoteAddedEvent) {
        if (event.emote.isAnimated)
            return

        val emote = "<:${event.emote.name}:${event.emote.id}>"
        val guildId = event.guild.id
        if (emotesCollection.findOne(BasicDBObject().append("guildId", guildId).append("emote", emote)) == null) {
            emotesCollection.insertOne(
                Emote(
                    guildId = guildId,
                    emote = emote,
                    usedCount = 0
                )
            )
        }
    }

    override fun onEmoteRemoved(event: EmoteRemovedEvent) {
        val emote = "<:${event.emote.name}:${event.emote.id}>"
        val guildId = event.guild.id
        emotesCollection.deleteOne(BasicDBObject().append("guildId", guildId).append("emote", emote))
    }

    override fun onGuildBan(event: GuildBanEvent) {
        event.guild.retrieveAuditLogs().type(ActionType.BAN).limit(5).queueAfter(5, TimeUnit.SECONDS) { banLogs ->
            val banLog = banLogs.firstOrNull { it.targetId == event.user.id }
            if (banLog?.targetType == TargetType.MEMBER) {
                val mod = banLog.user
                if (mod != null) {
                    event.guild.retrieveMember(mod).queue {
                        sendBanLog(event.user, it.user, banLog.reason, event.guild.id)
                    }
                }
            }
        }
    }

    override fun onGuildUnban(event: GuildUnbanEvent) {
        event.guild.retrieveAuditLogs().type(ActionType.UNBAN).limit(5).queueAfter(5, TimeUnit.SECONDS) { banLogs ->
            val banLog = banLogs.firstOrNull { it.targetId == event.user.id }
            if (banLog?.targetType == TargetType.MEMBER) {
                val mod = banLog.user
                if (mod != null) {
                    event.guild.retrieveMember(mod).queue {
                        sendUnbanLog(event.user, it.user, banLog.reason, event.guild.id)
                    }
                }
            }
        }
    }

    override fun onGuildMemberUpdateBoostTime(event: GuildMemberUpdateBoostTimeEvent) {
        val guildId = event.guild.id
        val boosterRole = guildId.boosterRole
        val boosterChannel = guildId.boosterChannel
        val boosterChat = guildId.boosterChat
        if (boosterRole.isEmpty() || boosterChannel.isEmpty()) {
            return
        }
        with(event.jda) {
            if (event.member.roles.map { it.id }.contains(boosterRole)) {
                getTextChannelById(boosterChannel)?.sendMessage("${event.member.user.asTag} just boosted $stonks")?.queue()
                if (boosterChat.isNotEmpty())
                    getTextChannelById(boosterChat)?.sendMessage("Hello ${event.member.asMention}! Thank you for boosting, please consider creating a custom role. See `${guildId.prefix}help colourme` for more info.")?.queue()
                else
                    return@with
            } else {
                getTextChannelById(boosterChannel)?.sendMessage("${event.member.user.asTag} just unboosted $stinks")?.queue()
            }
        }

    }

}
