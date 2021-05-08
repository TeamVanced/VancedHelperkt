package commands.utility

import commandhandler.CommandContext
import commands.base.BaseCommand
import type.CommandType.Utility
import config
import ext.optional
import utils.*
import java.text.DecimalFormat

class BAT : BaseCommand(
    commandName = "bat",
    commandDescription = "Check how many stonks we made today",
    commandArguments = mapOf("amount".optional()),
    commandType = Utility
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val json = "https://coinlib.io/api/v1/coin?key=${config.coinlibToken}&pref=EUR&symbol=BAT".getJson()

        val args = ctx.args
        val channel = ctx.event.channel

        val price = json?.string("price")?.toDouble()
        if (args.isNotEmpty()) {
            val amount = args[0].toIntOrNull()
            if (amount != null) {
                if (price != null) {
                    channel.sendMsg(
                        "${amount * price} EUR"
                    )
                }
            } else {
                channel.sendMsg("Provided argument is not a number!")
            }
        } else {
            channel.sendMsg(
                embedBuilder.apply {
                    setTitle("${json?.string("name")} (${json?.string("symbol")})")
                    addField(
                        "EUR",
                        price.apply { DecimalFormat("#.#####").format(this) }.toString(),
                        false
                    )
                    addField(
                        "Price Change",
                        "`1h` - ${json?.string("delta_1h")?.stonkify()}\n" +
                                "`24h` - ${json?.string("delta_24h")?.stonkify()}\n" +
                                "`7d` - ${json?.string("delta_7d")?.stonkify()}\n" +
                                "`30d` - ${json?.string("delta_30d")?.stonkify()}",
                        false
                    )
                    setFooter("Powered by coinlib.io")
                }.build()
            )
        }
    }

    private fun String.stonkify(): String {
        val price = Integer.parseInt(this.toDouble().toInt().toString(), 10)
        return when {
            price >= 25 -> "$relax $this%"
            price >= 5 -> "$vmerchant $this%"
            price >= 0 -> "$stonks $this%"
            price <= -5 -> "$feels $this%"
            price <= -25 -> "$sadness $this%"
            else -> "$stinks $this%"
        }
    }

}