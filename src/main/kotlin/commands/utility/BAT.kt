package commands.utility

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.util.*
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.rest.builder.interaction.int
import org.koin.core.component.inject
import repository.coin.CoinlibRepository

class BAT : BaseCommand(
    commandName = "bat",
    commandDescription = "Check how many stonks we made today"
) {

    private val repository by inject<CoinlibRepository>()

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val amount = ctx.args.integers["amount"]?.toInt()

        val response = repository.get(
            token = config.coinlibToken,
            pref = "EUR",
            symbol = "BAT"
        )

        val price = response.price

        if (amount != null) {
            ctx.respondPublic {
                content = "${amount * price} EUR"
            }
            return
        }

        ctx.respondPublic {
            embed {
                title = "${response.name} (${response.symbol})"
                field {
                    name = "EUR"
                    value = price.toString()
                }
                field("Price Change") {
                    """
                        `1h`  - ${response.delta1H.stonkify()}
                        `24h` - ${response.delta24H.stonkify()}
                        `7d`  - ${response.delta7D.stonkify()}
                        `30d` - ${response.delta30D.stonkify()}
                    """.trimIndent()
                }
                footer {
                    text = "Powered by coinlib.io"
                }
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                int(
                    name = "amount",
                    description = "Amount of BATs to convert to EUR",
                    builder = {
                        required = false
                    }
                )
            }
        )

    private fun Double.stonkify(): String =
        when {
            this >= 25 -> "$EMOTE_RELAX $this%"
            this >= 5 -> "$EMOTE_V_MERCHANT $this%"
            this >= 0 -> "$EMOTE_STONKS $this%"
            this <= -5 -> "$EMOTE_FEELS $this%"
            this <= -25 -> "$EMOTE_SADNESS $this%"
            else -> "$EMOTE_STINKS $this%"
        }

}