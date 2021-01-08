package ext

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import database.boosterRole
import database.collections.Warn
import database.modRoles
import database.warnsCollection
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import org.litote.kmongo.findOne

fun Member.isMod(guildId: String): Boolean {
    val modRoles = guildId.modRoles
    return roles.any { modRoles.contains(it.id) }
}

fun Member.isBooster(guildId: String): Boolean {
    val boosterRole = guildId.boosterRole
    return roles.any { boosterRole == it.id }
}

fun Member.warn(guildId: String, reason: String, channel: MessageChannel, embedBuilder: EmbedBuilder) {
    val filter = BasicDBObject("userId", user.id).append("guildId", guildId)
    if (warnsCollection.findOneAndUpdate(filter, Updates.push("reasons", reason)) == null) {
        warnsCollection.insertOne(
            Warn(
                guildId = guildId,
                userId = user.id,
                userName = user.asTag,
                reasons = listOf(reason)
            )
        )
    }
    embedBuilder.sendWarnLog(user, jda.selfUser, reason, guildId)
    if (warnsCollection.findOne(filter)?.reasons?.size == 3) {
        kick("Too many infractions").queue {
            channel.sendMessage("Kicked ${user.asTag}").queue()
            warnsCollection.deleteOne(filter)
        }
    }
}