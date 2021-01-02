package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import net.dv8tion.jda.api.entities.MessageChannel

class HowHaram : BaseCommand(
    commandName = "howharam",
    commandDescription = "Calculate haramness",
    commandType = Fun,
    commandArguments = listOf("[The thing]"),
    commandAliases = listOf("howhalal", "haram", "halal")
) {
    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            calculateHaram(args.joinToString(" "), ctx.channel)
        } else {
            val user = ctx.author.asMention
            calculateHaram(user, ctx.channel)
        }

    }

    private fun calculateHaram(arg: String, channel: MessageChannel) {
        val percentage = (0..100).random()
        val barAmount = percentage / 5
        val bar = "Halal " + "-".repeat(barAmount) + "\uD83D\uDD35" + "-".repeat(20 - barAmount) + " Haram"
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("Haram Calculator")
                setDescription("$arg is $percentage% Haram\n$bar")
            }.build()
        ).queue {
            messageId = it.id
        }
    }
}