package commands.moderation

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import database.warnsCollection
import ext.useArguments
import ext.useCommandProperly
import org.litote.kmongo.findOne

class Warns : BaseCommand(
    commandName = "warns",
    commandDescription = "Get warns for specified user",
    commandType = Moderation
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
            val filter = BasicDBObject("userId", id).append("guildId", guildId)
            val warn = warnsCollection.findOne(filter)
            if (warn != null) {
                val reasons = warn.reasons
                if (reasons.isNotEmpty()) {
                    channel.sendMessage(
                        embedBuilder.apply {
                            setTitle("Warns for ${warn.userName}")
                            for (i in reasons.indices) {
                                addField(
                                    "Warn ${i + 1}",
                                    reasons[i],
                                    false
                                )
                            }
                        }.build()
                    ).queueAddReaction()
                } else {
                    channel.sendMessage("User $user has no warns").queueAddReaction()
                }
            } else {
                channel.sendMessage("User $user has no warns").queueAddReaction()
            }
        } else {
            useArguments(1)
        }
    }

}