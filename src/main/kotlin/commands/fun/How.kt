package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import ext.optional
import ext.required
import ext.useArguments
import net.dv8tion.jda.api.entities.Message
import type.CommandType.Fun
import java.util.*

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
                ctx.message.calculate(predicate, thing)
            } else {
                ctx.message.calculate(predicate, ctx.author.asMention)
            }
        } else {
            ctx.message.useArguments(1)
        }
    }

    private fun Message.calculate(predicate: String, thing: String) {
        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)
        replyMsg(
            embedBuilder.apply {
                setTitle("${predicate.replaceFirstChar { it.titlecase(Locale.getDefault()) }.removeSuffix(" ")} Meter")
                setDescription("$thing is $percentage% $predicate\n$bar")
            }.build()
        )
    }

}