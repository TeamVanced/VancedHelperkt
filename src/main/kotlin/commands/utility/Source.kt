package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder

class Source : BaseCommand(
    name = "source",
    description = "Get a link to my github source"
){

    override suspend fun execute(
        ctx: CommandContext
    ) {
        ctx.respond {
            content = "<https://github.com/YTVanced/VancedHelperkt>"
        }
    }

    override suspend fun commandOptions(): CustomApplicationCommandCreateBuilder =
        CustomApplicationCommandCreateBuilder()

}