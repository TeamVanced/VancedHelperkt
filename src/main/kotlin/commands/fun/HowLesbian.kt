package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import net.dv8tion.jda.api.entities.MessageChannel

class HowLesbian : BaseCommand(
    commandName = "howlesbian",
    commandDescription = "Calculate lesbianness",
    commandType = Fun,
    commandArguments = listOf("[The thing]"),
    commandAliases = listOf("lesbian")
) {
    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            calculateLesbianness(args.joinToString(" "), ctx.channel)
        } else {
            val user = ctx.author.asMention
            calculateLesbianness(user, ctx.channel)
        }

    }

    private fun calculateLesbianness(arg: String, channel: MessageChannel) {
        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("Lesbian Meter")
                setDescription("$arg is $percentage% lesbian\n$bar")
            }.build()
        ).queueAddReaction()
    }
}