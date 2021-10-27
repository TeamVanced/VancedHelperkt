package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.string
import java.util.*

class EightBall : BaseCommand(
    commandName = "8ball",
    commandDescription = "Let a magical 8ball decide your fate"
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

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val question = ctx.args["question"]!!
            .string()
            .replace("(?i)are you(?-i)".toRegex(), "am I")
            .replace("(?i)will you(?-i)".toRegex(), "will I")
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
            .let {
                if (!it.endsWith("?")) {
                    return@let "$it?"
                }
                return@let it
            }

        ctx.respondPublic {
            embed {
                title = question
                description = responses.random()
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                string(
                    name = "question",
                    description = "A question for the magical ball",
                    builder = {
                        required = true
                    }
                )
            }
        )

}