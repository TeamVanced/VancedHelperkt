package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import utils.pressF

class F : BaseCommand(
    commandName = "f",
    commandDescription = "F",
    commandType = Fun,
    commandArguments = listOf("[Thing to pay respect for]")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            channel.sendMessage("$pressF ${ctx.author.name} pays respect for ${args.joinToString(" ")}").queueAddReaction()
        } else {
            channel.sendMessage(pressF).queueAddReaction()
        }
    }

}