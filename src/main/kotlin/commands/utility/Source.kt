package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
class Source : BaseCommand(
    commandName = "source",
    commandDescription = "Get a link to my github source"
){

    override suspend fun execute(
        ctx: CommandContext
    ) {
        ctx.respondPublic {
            content = "<https://github.com/YTVanced/VancedHelperkt>"
        }
    }

    override suspend fun commandOptions(): CustomApplicationCommandCreateBuilder =
        CustomApplicationCommandCreateBuilder()

}