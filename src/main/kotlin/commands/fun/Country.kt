package commands.`fun`

import com.beust.klaxon.JsonObject
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import config
import ext.hasQuotePerms
import ext.optional
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import utils.getJson

class Country : BaseCommand(
    commandName = "country",
    commandDescription = "Guess the country",
    commandType = Fun,
    commandArguments = mapOf("The thing".optional())
) {

    private val baseUrl = "https://gender-api.com/get-country-of-origin?key=${config.genderToken}"

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        if (ctx.authorAsMember?.hasQuotePerms(guildId) == false) {
            sendMessage("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            if (args[0].contains(contentIDRegex)) {
                ctx.guild.retrieveMemberById(contentIDRegex.find(args[0])!!.value).queue({
                    detectCountries(it.user.name.substringBefore(" "))
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER) {
                    sendMessage("Provided member does not exist!")
                }.handle(ErrorResponse.UNKNOWN_USER) {
                    sendMessage("Provided user does not exist!")
                })
            } else {
                detectCountries(args.joinToString(" "))
            }
        } else {
            detectCountries(event.author.name)
        }

    }

    private fun detectCountries(thing: String) {
        val json = "$baseUrl&name=$thing".getJson()
        val countries = json?.array<JsonObject>("country_of_origin")
        sendMessage(
            embedBuilder.apply {
                setTitle("Country Detector")
                if (countries != null && countries.isNotEmpty()) {
                    setDescription("Possible country(es) for $thing")
                    countries.take(5).forEach {
                        addField(
                            it.string("country_name"),
                            "Probability: ${it.double("probability")}",
                            false
                        )
                    }
                } else {
                    setDescription("Countries not found for $thing")
                }
                setFooter("Powered by gender-api.com")
            }.build()
        )
    }

}