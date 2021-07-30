package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.user

@OptIn(KordPreview::class)
class Avatar : BaseCommand(
    name = "avatar",
    description = "Get user's avatar"
){

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val user = ctx.args["user"]!!.user()

        ctx.respond {
            embed {
                title = "${user.username}'s avatar"
                description = "[Avatar URL](${user.avatar.url})"
                image = "${user.avatar.url}?size=256"
            }
        }
    }

    override suspend fun commandOptions() = CustomApplicationCommandCreateBuilder(
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