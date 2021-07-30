package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.int
import dev.kord.core.entity.interaction.string

@OptIn(KordPreview::class)
class Embed : BaseCommand(
    name = "embed",
    description = "Create an embed"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val title = ctx.args["title"]?.string()
        val description = ctx.args["description"]?.string()
        val image = ctx.args["image"]?.string()
        val color = ctx.args["color"]?.int()

        ctx.respond {
            embed {
                this.title = title
                this.description = description
                this.image = image
                this.color = if (color != null) Color(color) else null
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