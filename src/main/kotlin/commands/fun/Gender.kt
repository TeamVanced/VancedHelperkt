package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import config
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.user
import org.koin.core.component.inject
import repository.gender.GenderRepositoryImpl

@OptIn(KordPreview::class)
class Gender : BaseCommand(
    name = "gender",
    description = "Get the gender of a user"
) {

    private val repository by inject<GenderRepositoryImpl>()

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val user = ctx.args["user"]!!.user()
        val userName = user.username.filter { it.isLetter() }

        val response = repository.get(
            token = config.genderToken,
            name = userName
        )

        ctx.respond {
            embed {
                title = "Gender Detector"
                description = "$userName is ${response.gender}\nAccuracy: ${response.accuracy}%"

                footer {
                    text = "Powered by gender-api.com"
                }
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                user(
                    name = "user",
                    description = "Whose gender to get",
                    builder = {
                        required = true
                    }
                )
            }
        )
}