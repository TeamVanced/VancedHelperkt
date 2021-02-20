package ext

import database.errorChannel
import database.modlogChannel
import jda
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import java.awt.Color

fun Guild.sendStacktrace(title: String?, stacktrace: String?) {
    val errorChannel = id.errorChannel
    if (errorChannel.isEmpty()) {
        return
    }
    getTextChannelById(errorChannel)?.sendMessage(
        MessageBuilder().setEmbed(
            EmbedBuilder().setColor(Color.RED).apply {
                setTitle(title)
                setDescription(stacktrace?.take(2045) + "...")
            }.build()
        ).setContent("423915768191647755".asMention).build()
    )?.queue()
}

fun sendModLog(title: String, user: User, mod: User, reason: String?, guildId: String) {
    jda?.getTextChannelById(guildId.modlogChannel)?.sendMessage(
        EmbedBuilder().setColor(Color.MAGENTA).apply {
            setTitle(title)
            addField(
                "User",
                user.getModLogInfo(),
                false
            )
            addField(
                "Moderator",
                mod.getModLogInfo(),
                false
            )
            if (reason != null) {
                addField(
                    "Reason",
                    reason,
                    false
                )
            }
        }.build()
    )?.queue()
}

fun sendWarnLog(user: User, mod: User, reason: String?, guildId: String) {
    sendModLog("User Warned", user, mod, reason, guildId)
}

fun sendMuteLog(user: User, mod: User, reason: String?, guildId: String) {
    sendModLog("User Muted", user, mod, reason, guildId)
}

fun sendBanLog(user: User, mod: User, reason: String?, guildId: String) {
    sendModLog("User Banned", user, mod, reason, guildId)
}

fun sendUnbanLog(user: User, mod: User, reason: String?, guildId: String) {
    sendModLog("User Unbanned", user, mod, reason, guildId)
}

fun sendUnwarnLog(user: User, mod: User, guildId: String) {
    sendModLog("User Unwarned", user, mod, null, guildId)
}

fun sendUnmuteLog(user: User, mod: User, guildId: String) {
    sendModLog("User Unmuted", user, mod, null, guildId)
}