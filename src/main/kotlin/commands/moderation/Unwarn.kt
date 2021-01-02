package commands.moderation

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import database.warnsCollection
import ext.sendUnwarnLog
import ext.useArguments
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Unwarn : BaseCommand(
    commandName = "unwarn",
    commandDescription = "Unwarn a user",
    commandType = Moderation,
    commandArguments = listOf("<User ID | User Mention> <Warn number>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val warnIndex = args[1]
            if (warnIndex.toIntOrNull() == null) {
                channel.sendMessage("$warnIndex is not a valid warn").queue {
                    messageId = it.id
                }
                return
            }
            val id = user.filter { it.isDigit() }
            val filter = BasicDBObject("userId", id).append("guildId", guildId)
            ctx.guild.retrieveMemberById(id).queue({ member ->
                if (warnsCollection.findOneAndUpdate(
                        filter,
                        Updates.unset("reasons.${warnIndex.toInt() - 1}")
                    ) != null
                ) {
                    warnsCollection.findOneAndUpdate(filter, Updates.pull("reasons", null))
                    channel.sendMessage("Successfully unwarned ${member.user.asMention}").queue {
                        messageId = it.id
                    }
                    ctx.authorAsMember?.let { embedBuilder.sendUnwarnLog(member, it, guildId) }
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                channel.sendMessage("Provided user does not exist!").queue {
                    messageId = it.id
                }
            })

        } else {
            channel.useArguments(1, this)
        }
    }

}