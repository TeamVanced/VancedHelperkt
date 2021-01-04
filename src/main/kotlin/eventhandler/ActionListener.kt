package eventhandler

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import database.*
import database.collections.Emote
import ext.sendBanLog
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.audit.TargetType
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.litote.kmongo.findOne
import utils.stinks
import utils.stonks
import java.awt.Color

class ActionListener : ListenerAdapter() {

    private val embedBuilder get() = EmbedBuilder().setColor(Color.pink)
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
            guild.emotes.forEach {
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

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        super.onGuildMessageReceived(event)
        val message = event.message.contentRaw
        val guildId = event.guild.id
        if (message.contains(emoteRegex)) {
            val emote = emoteRegex.findAll(message)
            val filter = BasicDBObject().append("guildId", guildId)
            emote.forEach {
                val emoteFilter = filter.append("emote", it.value)
                val emoteCollection = emotesCollection.findOne(emoteFilter)
                if (emoteCollection == null) {
                    emotesCollection.insertOne(
                        Emote(
                            guildId = guildId,
                            emote = it.value,
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
        event.guild.retrieveAuditLogs().type(ActionType.BAN).limit(1).queue {
            val banLog = it[0]
            if (banLog.targetType == TargetType.MEMBER) {
                event.guild.retrieveMember(event.user).queue { mod ->
                    embedBuilder.sendBanLog(banLog.targetId, mod, banLog.reason, event.guild.id)
                }
            }
        }
    }

    override fun onGuildMemberUpdateBoostTime(event: GuildMemberUpdateBoostTimeEvent) {
        super.onGuildMemberUpdateBoostTime(event)
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