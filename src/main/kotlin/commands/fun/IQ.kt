package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import net.dv8tion.jda.api.entities.MessageChannel

class IQ : BaseCommand(
    commandName = "iq",
    commandDescription = "Calculate IQ level",
    commandType = Fun,
    commandArguments = listOf("[The thing]")
) {
    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            calculateIQ(args.joinToString(" "), ctx.channel)
        } else {
            val user = ctx.author.asMention
            calculateIQ(user, ctx.channel)
        }

    }

    private fun calculateIQ(arg: String, channel: MessageChannel) {
        val iq = IQLogic()
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("IQ Calculator")
                setDescription("$arg has an iq of $iq")
            }.build()
        ).queueAddReaction()
    }
}