package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.user
import org.apache.commons.math3.distribution.NormalDistribution

@OptIn(KordPreview::class)
class IQ : BaseCommand(
    commandName = "iq",
    commandDescription = "Calculate user's IQ"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val user = ctx.args["user"]!!.user()

        ctx.respond {
            embed {
                title = "IQ Calculator"
                description = "${user.mention} has an IQ of ${randomIQ()}"
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                user(
                    name = "user",
                    description = "Whose IQ to calculate",
                    builder = {
                        required = true
                    }
                )
            }
        )

    private fun randomIQ(): Int {
        val iqDist = NormalDistribution(100.0, 15.0)
        val randomIq = (50..150).random()
        val luck = Math.random()
        val cumulative = iqDist.cumulativeProbability(randomIq.toDouble())
        val smallo = cumulative < 0.5
        return if (smallo && cumulative > luck || !smallo && cumulative < luck) {
            randomIq
        } else {
            randomIQ()
        }
    }

}