package ext

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import database.boosterRole
import database.collections.Warn
import database.modRoles
import database.warnsCollection
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.exceptions.HierarchyException
import org.litote.kmongo.findOne

fun Member.isMod(guildId: String): Boolean {
    val modRoles = guildId.modRoles
    return roles.any { modRoles.contains(it.id) }
}

fun Member.isBooster(guildId: String): Boolean {
    val boosterRole = guildId.boosterRole
    return roles.any { boosterRole == it.id }
}

fun Member.warn(mod: User, guildId: String, reason: String, channel: TextChannel) {
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
    sendWarnLog(user, mod, reason, guildId)
    if (warnsCollection.findOne(filter)?.reasons?.size == 3) {
        try {
            kick("Too many infractions").queue {
                channel.sendMessageWithChecks("Kicked ${user.asTag}")
                warnsCollection.deleteOne(filter)
            }
        } catch (e: HierarchyException) {}
    }
}