package commands.moderation

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Moderation
import database.warnsCollection
import ext.*
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Unwarn : BaseCommand(
    commandName = "unwarn",
    commandDescription = "Unwarn a user",
    commandType = Moderation,
    commandArguments = mapOf("User ID | User Mention".required(), "warn number | all".optional())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val id = user.filter { it.isDigit() }
            if (id.isEmpty()) {
                useCommandProperly()
                return
            }
            fun removeWarn(removeAction: (member: Member) -> Unit) {
                ctx.guild.retrieveMemberById(id).queue({ member ->
                    removeAction(member)
                    channel.sendMessage("Successfully unwarned ${member.user.asMention}").queueAddReaction()
                    ctx.authorAsMember?.let { embedBuilder.sendUnwarnLog(member.user, it.user, guildId) }
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                    channel.sendMessage("Provided user does not exist!").queueAddReaction()
                })
            }
            val filter = BasicDBObject("userId", id).append("guildId", guildId)
            when (args.size) {
                1 -> removeWarn {
                    warnsCollection.findOneAndUpdate(filter, Updates.popLast("reasons"))
                }
                2 -> {
                    if (args[1] == "all") {
                        removeWarn {
                            warnsCollection.findOneAndDelete(filter)
                        }
                    } else {
                        val warnIndex = args[1]
                        if (warnIndex.toIntOrNull() == null) {
                            channel.sendMessage("$warnIndex is not a valid warn").queueAddReaction()
                            return
                        }
                        removeWarn {
                            if (warnsCollection.findOneAndUpdate(
                                    filter,
                                    Updates.unset("reasons.${warnIndex.toInt() - 1}")
                                ) != null
                            ) {
                                warnsCollection.findOneAndUpdate(filter, Updates.pull("reasons", null))
                            }
                        }
                    }
                }
            }
        } else {
            useArguments(1)
        }
    }

}