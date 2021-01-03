package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class PPSize : BaseCommand(
    commandName = "ppsize",
    commandDescription = "Calculate PP size",
    commandType = Fun,
    commandAliases = listOf("pp", "coce"),
    commandArguments = listOf("[The thing]")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            calculatePPSize(args.joinToString(" "), event)
        } else {
            calculatePPSize(event.author.asMention, event)
        }

    }

    private fun calculatePPSize(thing: String, event: GuildMessageReceivedEvent) {
        val ppsize = (2..14).random()
        val bar = "8" + "=".repeat(ppsize) + "D"
        event.channel.sendMessage(
            embedBuilder.apply {
                setTitle("PP Size Calculator")
                setDescription("$thing has a PP size of $ppsize inches\n$bar")
            }.build()
        ).queueAddReaction()
    }

}