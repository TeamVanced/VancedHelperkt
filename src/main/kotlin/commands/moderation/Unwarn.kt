package commands.moderation

import com.mongodb.BasicDBObject
import com.mongodb.client.model.Updates
import commandhandler.CommandContext
import commands.base.BaseCommand
import database.warnsCollection
import ext.*
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import type.CommandType.Moderation

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
                ctx.message.useCommandProperly()
                return
            }
            fun removeWarn(removeAction: (member: Member) -> Unit) {
                ctx.guild.retrieveMemberById(id).queue({ member ->
                    removeAction(member)
                    ctx.message.replyMsg("Successfully unwarned ${member.user.asMention}")
                    ctx.authorAsMember?.let { sendUnwarnLog(member.user, it.user, guildId) }
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                    ctx.message.replyMsg("Provided user does not exist!")
                }.handle(ErrorResponse.UNKNOWN_MEMBER) {
                    ctx.message.replyMsg("Provided member does not exist!")
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
                            ctx.message.replyMsg("$warnIndex is not a valid warn")
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
            ctx.message.useArguments(1)
        }
    }

}