package commands.utility

import config

import core.command.CommandContext
import core.command.base.BaseCommand
import core.const.*
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.int
import org.koin.core.component.inject
import repository.coin.CoinRepositoryImpl

@OptIn(KordPreview::class)
class BAT : BaseCommand(
    name = "bat",
    description = "Check how many stonks we made today"
) {

    private val repository by inject<CoinRepositoryImpl>()

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val amount = ctx.args["amount"]?.int()

        val response = repository.get(
            token = config.coinlibToken,
            pref = "EUR",
            symbol = "BAT"
        )

        val price = response.price

        if (amount != null) {
            ctx.respond {
                content = "${amount * price} EUR"
            }
            return
        }

        ctx.respond {
            embed {
                title = "${response.name} (${response.symbol})"
                field {
                    name = "EUR"
                    value = price.toString()
                }
                field {
                    name = "Price Change"
                    value = """
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

    override suspend fun commandOptions() = CustomApplicationCommandCreateBuilder(
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
            this >= 25 -> "$relax $this%"
            this >= 5 -> "$vmerchant $this%"
            this >= 0 -> "$stonks $this%"
            this <= -5 -> "$feels $this%"
            this <= -25 -> "$sadness $this%"
            else -> "$stinks $this%"
        }

}