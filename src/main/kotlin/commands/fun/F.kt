package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.const.pressF
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.string

@OptIn(KordPreview::class)
class F : BaseCommand(
    name = "f",
    description = "Pay respects"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val thing = ctx.args["thing"]?.string()

        ctx.respond {
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