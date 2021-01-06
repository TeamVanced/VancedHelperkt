package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import ext.useArguments

class How : BaseCommand(
    commandName = "how",
    commandDescription = "See how [predicate] is [the thing]",
    commandType = Fun,
    commandArguments = listOf("<predicate> [| <the thing>]")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val joinedArray = args.joinToString(" ")
            val predicate = joinedArray.substringBefore("|")
            val thing = joinedArray.substringAfter("|", "")
            if (thing.isNotEmpty()) {
                println(thing)
                calculate(predicate, thing)
            } else {
                calculate(predicate, ctx.author.asMention)
            }
        } else {
            useArguments(1)
        }
    }

    private fun calculate(predicate: String, thing: String) {
        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("${predicate.capitalize().removeSuffix(" ")} Meter")
                setDescription("$thing is $percentage% $predicate\n$bar")
            }.build()
        ).queueAddReaction()
    }

}