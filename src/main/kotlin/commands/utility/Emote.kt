package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.string

class Emote : BaseCommand(
    commandName = "emote",
    commandDescription = "Get a corresponding emote link"
) {

    data class ParsedEmote(
        val id: String,
        val isAnimated: Boolean
    )

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val emoteRegex = """<?(a)?:?(\w{2,32}):(\d{17,19})>?""".toRegex()

        val emotes = ctx.args.strings["emotes"]!!

        val parsedEmotes = emoteRegex.findAll(emotes).map {
            ParsedEmote(
                id = it.groups[3]!!.value,
                isAnimated = it.groups[1] != null,
            )
        }.toList()

        if (parsedEmotes.isEmpty()) {
            ctx.respondEphemeral {
                content = "No emotes were provided"
            }
            return
        }

        ctx.respondEphemeral {
            content = parsedEmotes.distinct().joinToString("\n") { emote ->
                "https://cdn.discordapp.com/emojis/${emote.id}.${if (emote.isAnimated) "gif" else "png"}"
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                string(
                    name = "emotes",
                    description = "Emotes to parse",
                    builder = {
                        required = true
                    }
                )
            }
        )

}