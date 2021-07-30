package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.string

@OptIn(KordPreview::class)
class How : BaseCommand(
    name = "how",
    description = "See how [predicate] is [the thing]"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val predicate = ctx.args["predicate"]!!.string()
        val thing = ctx.args["thing"]!!.string()
            .replace("(?i)you(?-i)".toRegex(), "I")
            .replace("(?i)me(?-i)".toRegex(), "You")

        val percentage = (0..100).random()
        val barAmount = percentage / 10
        val bar = "▰".repeat(barAmount) + "▱".repeat(10 - barAmount)

        val convertedDescription = "$thing is $percentage% $predicate"
            .replace("(?i)you is(?-i)".toRegex(), "You are")
            .replace("(?i)I is(?-i)".toRegex(), "I am")

        ctx.respond {
            embed {
                title = "$predicate meter"
                description = "$convertedDescription\n$bar"
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                string(
                    name = "predicate",
                    description = "Verb",
                    builder = {
                        required = true
                    }
                )
                string(
                    name = "thing",
                    description = "The thing to judge",
                    builder = {
                        required = true
                    }
                )
            }
        )


}