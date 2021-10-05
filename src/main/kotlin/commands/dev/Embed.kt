package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.ext.isDev
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.Color
import dev.kord.core.entity.interaction.int
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string

//TODO this sucks
class Embed : BaseCommand(
    commandName = "embed",
    commandDescription = "Create an embed"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        if (!ctx.author.isDev) {
            return ctx.respondEphemeral {
                content = "You're not allowed to execute this command"
            }
        }

        val embedColor = ctx.args["color"]?.int()

        ctx.respondPublic {
            embed {
                title = ctx.args["title"]?.string()
                description = ctx.args["description"]?.string()
                image = ctx.args["image"]?.string()
                color = if (embedColor != null) Color(embedColor) else null
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                string(
                    name = "title",
                    description = "Title of the embed",
                    builder = {
                        required = false
                    }
                )
                string(
                    name = "description",
                    description = "Description of the embed",
                    builder = {
                        required = false
                    }
                )
                string(
                    name = "image",
                    description = "Link to the image",
                    builder = {
                        required = false
                    }
                )
                int(
                    name = "color",
                    description = "Color of the embed",
                    builder = {
                        required = false
                    }
                )
            }
        )

}