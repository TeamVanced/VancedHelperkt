package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility
import config
import utils.*
import java.text.DecimalFormat
import kotlin.math.roundToInt

class BAT : BaseCommand(
    commandName = "bat",
    commandDescription = "Check how many stonks we made today",
    commandType = Utility
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val json = "https://coinlib.io/api/v1/coin?key=${config.coinlibToken}&pref=EUR&symbol=BAT".getJson()
        ctx.channel.sendMessage(
            embedBuilder.apply {
                setTitle("${json?.string("name")} (${json?.string("symbol")})")
                addField(
                    "EUR",
                    json?.string("price")?.toDouble().apply { DecimalFormat("#.#####").format(this) }.toString(),
                    false
                )
                addField(
                    "Price Change",
                    "`1h` - ${json?.string("delta_1h")?.toDouble()?.stonkify()}\n" +
                    "`24h` - ${json?.string("delta_24h")?.toDouble()?.stonkify()}\n" +
                    "`7d` - ${json?.string("delta_7d")?.toDouble()?.stonkify()}\n" +
                    "`30d` - ${json?.string("delta_30d")?.toDouble()?.stonkify()}",
                    false
                )
                setFooter("Powered by coinlib.io")
            }.build()
        ).queueAddReaction()
    }

    private fun Double.stonkify(): String {
        val roundedPrice = roundToInt()
        return when {
            roundedPrice >= 25 -> "$relax $this%"
            roundedPrice >= 5 -> "$vmerchant $this%"
            roundedPrice == 1 -> "$stonks $this%"
            roundedPrice <= -5 -> "$feels $this%"
            roundedPrice <= -25 -> "$sadness $this%"
            else -> "$stinks $this%"
        }
    }

}