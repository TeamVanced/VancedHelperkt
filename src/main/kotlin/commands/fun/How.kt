package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import ext.optional
import ext.required
import ext.useArguments
import net.dv8tion.jda.api.entities.TextChannel
import type.CommandType.Fun

class How : BaseCommand(
    commandName = "how",
    commandDescription = "See how [predicate] is [the thing]",
    commandType = Fun,
    commandArguments = mapOf("predicate".required(), "| the thing".optional())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val joinedArray = args.joinToString(" ")
            val predicate = joinedArray.substringBefore("|")
            val thing = joinedArray.substringAfter("|", "")
            if (thing.isNotEmpty()) {
                ctx.channel.calculate(predicate, thing)
            } else {
                ctx.channel.calculate(predicate, ctx.author.asMention)
            }
        } else {
            ctx.channel.useArguments(1)
        }
    }

    private fun TextChannel.calculate(predicate: String, thing: String) {
        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)
        sendMsg(
            embedBuilder.apply {
                setTitle("${predicate.capitalize().removeSuffix(" ")} Meter")
                setDescription("$thing is $percentage% $predicate\n$bar")
            }.build()
        )
    }

}