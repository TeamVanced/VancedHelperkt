package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.core.entity.interaction.user
import dev.kord.rest.builder.interaction.user

class Avatar : BaseCommand(
    commandName = "avatar",
    commandDescription = "Get user's avatar"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val user = ctx.args["user"]!!.user()

        ctx.respondPublic {
            embed {
                val avatarUrl = user.avatar?.url
                    ?: "https://cdn.discordapp.com/embed/avatars/${user.discriminator.toInt() % 5}.png"

                title = "${user.username}'s avatar"
                description = "[Avatar URL](${avatarUrl}?size=512)"
                image = "${avatarUrl}?size=256"
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                user(
                    name = "user",
                    description = "Whose avatar to get",
                    builder = {
                        required = true
                    }
                )
            }
        )

}