package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import ext.required
import ext.useArguments
import type.CommandType.Fun

class EightBall : BaseCommand(
    commandName = "8ball",
    commandDescription = "Let the magic 8ball decide your fate",
    commandType = Fun,
    commandArguments = mapOf("Question".required()),
    commandAliases = listOf("8b", "fortune")
) {

    private val responses = arrayOf(
        "It is certain",
        "It is decidedly so",
        "Without a doubt",
        "Yes, definitely",
        "You may rely on it",
        "As I see it, yes",
        "Most likely",
        "Outlook good",
        "Yes",
        "Signs point to yes",
        "Reply hazy, try again",
        "Ask again later",
        "Better not tell you now",
        "Cannot predict now",
        "Concentrate and ask again",
        "Don't count on it",
        "My reply is no",
        "My sources say no",
        "Outlook not so good",
        "Very doubtful"
    )

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        if (ctx.args.isNotEmpty()) {
            ctx.event.channel.sendMsg(responses.random())
        } else {
            ctx.channel.useArguments(1)
        }

    }

}