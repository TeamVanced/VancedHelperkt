package eventhandler

import database.*
import ext.sendBanLog
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class ActionListener : ListenerAdapter() {

    private val embedBuilder get() = EmbedBuilder().setColor(Color.pink)

    override fun onReady(event: ReadyEvent) {
        event.jda.guilds.forEach {
            val logChannel = it.id.logChannel
            if (logChannel.isNotEmpty()) {
                event.jda.getTextChannelById(logChannel)?.sendMessage(
                    EmbedBuilder().apply {
                        setTitle("I just started!")
                        setDescription(
                            "**Prefix**: `${it.id.prefix}`\n" +
                            "**Guilds**: `${event.jda.guilds.size}`\n" +
                            "**Channels**: `${it.channels.size}\n`" +
                            "**Members**: `${it.memberCount}`"
                        )
                    }.build()
                )?.queue()
            }
        }
    }

    override fun onGuildBan(event: GuildBanEvent) {
        event.guild.retrieveAuditLogs().type(ActionType.BAN).limit(1).queue {
            val banLog = it[0]
            event.guild.retrieveMemberById(banLog.targetId).queue { member ->
                event.guild.retrieveMember(event.user).queue { mod ->
                    embedBuilder.sendBanLog(member, mod, banLog.reason, event.guild.id)
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
                getTextChannelById(boosterChannel)?.sendMessage("${event.member.user.asTag} just boosted :stonks:")
                    ?.queue()
                if (boosterChat.isNotEmpty())
                    getTextChannelById(boosterChat)?.sendMessage("Hello ${event.member.asMention}! Thank you for boosting, please consider creating a custom role. see ${guildId.prefix}help colourme for more info.")
                        ?.queue()
                else
                    return@with
            } else {
                getTextChannelById(boosterChannel)?.sendMessage("${event.member.user.asTag} just unboosted :stinks:")
                    ?.queue()
            }
        }

    }

}