package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.util.EMOTE_PRESS_F
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.string

class F : BaseCommand(
    commandName = "f",
    commandDescription = "Pay respects"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val thing = ctx.args.strings["thing"]

        ctx.respondPublic {
            content = if (thing != null) {
                "$EMOTE_PRESS_F ${ctx.author.mention} pays respects for $thing"
            } else {
                EMOTE_PRESS_F
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