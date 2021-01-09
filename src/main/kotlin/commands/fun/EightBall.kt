package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import ext.required
import ext.useArguments

class EightBall : BaseCommand(
    commandName = "8ball",
    commandDescription = "Let the magic 8ball decide your fate",
    commandType = Fun,
    commandArguments = mapOf("Question".required())
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
            channel.sendMessage(responses.random()).queueAddReaction()
        } else {
            useArguments(1)
        }

    }

}