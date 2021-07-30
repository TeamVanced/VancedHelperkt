package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.string

class Emote : BaseCommand(
    name = "emote",
    description = "Get a corresponding emote link"
) {

    @OptIn(KordPreview::class)
    override suspend fun execute(
        ctx: CommandContext
    ) {
        val emoteRegex = "<?(a)?:?(\\w{2,32}):(\\d{17,19})>?".toRegex()

        val emotes = ctx.args["emotes"]!!.string()

        val parsedEmoteIds = emoteRegex.findAll(emotes).map {
            it.groupValues[3]
        }.toList()
        val emoteLinks = mutableListOf<String>()

        if (parsedEmoteIds.isEmpty()) {
            ctx.respond {
                content = "No emotes were provided"
            }
            return
        }

        parsedEmoteIds.forEach { emoteId ->
            val suffix = if (emoteId.startsWith("<a")) "gif" else "png"
            val link = "https://cdn.discordapp.com/emojis/$emoteId.$suffix"
            emoteLinks.add(link)
        }

        val filteredEmoteLinks = emoteLinks.distinct()
        ctx.respond {
            content = filteredEmoteLinks.joinToString("\n") {
                if (filteredEmoteLinks.size > 1) {
                    "<$it>"
                } else it
            }
        }
    }

    @OptIn(KordPreview::class)
    override suspend fun commandOptions() = CustomApplicationCommandCreateBuilder(
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