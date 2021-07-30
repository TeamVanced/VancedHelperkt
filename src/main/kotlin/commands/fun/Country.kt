package commands.`fun`

import core.command.CommandContext
import config

import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.user
import org.koin.core.component.inject
import repository.country.CountryRepositoryImpl

class Country : BaseCommand(
    name = "country",
    description = "Get the country of a user"
) {

    private val repository by inject<CountryRepositoryImpl>()

    @OptIn(KordPreview::class)
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
                title = "Country Detector"

                if (response.countries.isNotEmpty()) {
                    description = "Possible countries for ${user.mention}"

                    response.countries.take(5).forEach {
                        field {
                            name = it.countryName
                            value = "Probability: ${it.probability}%"
                        }
                    }
                } else {
                    description = "Countries not found for ${user.mention}"
                }

                footer {
                    text = "Powered by gender-api.com"
                }
            }
        }
    }

    @OptIn(KordPreview::class)
    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                user(
                    name = "user",
                    description = "Whose country to get",
                    builder = {
                        required = true
                    }
                )
            }
        )

}