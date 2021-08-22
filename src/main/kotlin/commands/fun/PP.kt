package commands.`fun`

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.user
import org.apache.commons.math3.distribution.NormalDistribution

@OptIn(KordPreview::class)
class PP : BaseCommand(
    commandName = "pp",
    commandDescription = "Calculate user's PP size"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val user = ctx.args["user"]!!.user()

        val ppSize = randomPP()
        val bar = "8" + "=".repeat(ppSize) + "D"
        ctx.respondPublic {
            embed {
                title = "PP size Calculator"
                description = "${user.mention} has a PP size of $ppSize inches\n$bar"
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                user(
                    name = "user",
                    description = "Whose PP size to calculate",
                    builder = {
                        required = true
                    }
                )
            }
        )

    private fun randomPP(): Int {
        val ppDist = NormalDistribution(6.0, 1.0)
        val randomPp = (2..14).random()
        val luck = Math.random()
        val cumulative = ppDist.cumulativeProbability(randomPp.toDouble())
        val smallo = cumulative < 0.5
        return if (smallo && cumulative > luck || !smallo && cumulative < luck) {
            randomPp
        } else {
            randomPP()
        }
    }

}