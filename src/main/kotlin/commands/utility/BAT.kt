package commands.utility

import commandhandler.CommandContext
import commands.base.BaseCommand
import config
import ext.optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import repository.coin.CoinRepositoryImpl
import type.CommandType.Utility
import utils.*

class BAT : BaseCommand(
    commandName = "bat",
    commandDescription = "Check how many stonks we made today",
    commandArguments = mapOf("amount".optional()),
    commandType = Utility
) {

    private val repository by inject<CoinRepositoryImpl>()

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.get(
                    token = config.coinlibToken,
                    pref = "EUR",
                    symbol = "BAT"
                )

                val args = ctx.args
                val message = ctx.message

                val price = response.price
                if (args.isNotEmpty()) {
                    val amount = args[0].toIntOrNull()
                    if (amount != null) {
                        message.replyMsg(
                            "${amount * price} EUR"
                        )
                    } else {
                        message.replyMsg("Provided argument is not a number!")
                    }
                } else {
                    message.replyMsg(
                        embedBuilder.apply {
                            setTitle("${response.name} (${response.symbol})")
                            addField(
                                "EUR",
                                price.toString(),
                                false
                            )
                            addField(
                                "Price Change",
                                "`1h` - ${response.delta1H.stonkify()}\n" +
                                        "`24h` - ${response.delta24H.stonkify()}\n" +
                                        "`7d` - ${response.delta7D.stonkify()}\n" +
                                        "`30d` - ${response.delta30D.stonkify()}",
                                false
                            )
                            setFooter("Powered by coinlib.io")
                        }.build()
                    )
                }
            } catch (e: Exception) {
                ctx.message.replyMsg("An error occurred while trying to fetch data")
            }
        }
    }

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