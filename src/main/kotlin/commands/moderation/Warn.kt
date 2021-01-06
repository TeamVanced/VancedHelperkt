package commands.moderation

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import database.collections.Warn
import database.warnsCollection
import ext.sendWarnLog
import ext.useArguments
import ext.useCommandProperly
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import org.litote.kmongo.findOne

class Warn : BaseCommand(
    commandName = "warn",
    commandDescription = "Warn a user",
    commandType = Moderation,
    commandArguments = listOf("<User ID | User Mention>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val reason = if (args.size > 1) args.apply { remove(user) }.joinToString(" ") else "no reason provided"
            val id = user.filter { it.isDigit() }
            val filter = BasicDBObject("userId", id).append("guildId", guildId)
            if (id.isEmpty()) {
                useCommandProperly()
                return
            }
            ctx.guild.retrieveMemberById(id).queue({ member ->
                if (!ctx.authorAsMember?.canInteract(member)!!) {
                    channel.sendMessage("You can't warn this member!").queueAddReaction()
                    return@queue
                }
                if (warnsCollection.findOneAndUpdate(filter, Updates.push("reasons", reason)) == null) {
                    warnsCollection.insertOne(
                        Warn(
                            guildId = guildId,
                            userId = id,
                            userName = member.user.asTag,
                            reasons = listOf(reason)
                        )
                    )
                }
                channel.sendMessage("Successfully warned ${member.user.asMention}").queue {
                    messageId = it.id
                }
                ctx.authorAsMember?.let { embedBuilder.sendWarnLog(member, it, reason, guildId) }
                if (warnsCollection.findOne(filter)?.reasons?.size == 3) {
                    member.kick("Too many infractions").queue {
                        channel.sendMessage("Kicked ${member.user.asTag}").queueAddReaction()
                        warnsCollection.deleteOne(filter)
                    }
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                channel.sendMessage("Provided user does not exist!").queueAddReaction()
            })

        } else {
            useArguments(1)
        }
    }

}