package commands.vanced

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility

class SupportUs : BaseCommand(
    commandName = "supportus",
    commandDescription = "learn how to support us",
    commandType = Utility
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)

    }

}