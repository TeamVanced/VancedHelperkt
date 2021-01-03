package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import net.dv8tion.jda.api.entities.MessageChannel

class HowGay : BaseCommand(
    commandName = "howgay",
    commandDescription = "Calculate gayness",
    commandType = Fun,
    commandArguments = listOf("[The thing]"),
    commandAliases = listOf("gay")
) {
    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            calculateGayness(args.joinToString(" "), ctx.channel)
        } else {
            val user = ctx.author.asMention
            calculateGayness(user, ctx.channel)
        }

    }

    private fun calculateGayness(arg: String, channel: MessageChannel) {
        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("Gay Meter")
                setDescription("$arg is $percentage% gay\n$bar")
            }.build()
        ).queueAddReaction()
    }
}