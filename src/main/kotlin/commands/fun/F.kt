package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.const.pressF
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.string

class F : BaseCommand(
    commandName = "f",
    commandDescription = "Pay respects"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val thing = ctx.args["thing"]?.string()

        ctx.respondPublic {
            content = if (thing != null) {
                "$pressF ${ctx.author.mention} pays respects for $thing"
            } else {
                pressF
            }
        }
    }

    override suspend fun commandOptions() = CustomApplicationCommandCreateBuilder(
        arguments = {
            string(
                name = "thing",
                description = "The thing to pay respects for",
                builder = {
                    required = false
                }
            )
        }
    )

}