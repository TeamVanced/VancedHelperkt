package commands.`fun`

import com.beust.klaxon.JsonObject
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import config
import ext.hasQuotePerms
import ext.optional
import net.dv8tion.jda.api.entities.TextChannel
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
            ctx.event.channel.sendMsg("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            if (args[0].contains(contentIDRegex)) {
                ctx.guild.retrieveMemberById(contentIDRegex.find(args[0])!!.value).queue({
                    ctx.channel.detectCountries(it.user.name.substringBefore(" "))
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER) {
                    ctx.event.channel.sendMsg("Provided member does not exist!")
                }.handle(ErrorResponse.UNKNOWN_USER) {
                    ctx.event.channel.sendMsg("Provided user does not exist!")
                })
            } else {
                ctx.channel.detectCountries(args.joinToString(" "))
            }
        } else {
            ctx.channel.detectCountries(event.author.name)
        }

    }

    private fun TextChannel.detectCountries(thing: String) {
        val filteredThing = thing.filter { it.isLetter() }
        val json = "$baseUrl&name=$filteredThing".getJson()
        val countries = json?.array<JsonObject>("country_of_origin")
        sendMsg(
            embedBuilder.apply {
                setTitle("Country Detector")
                if (countries != null && countries.isNotEmpty()) {
                    setDescription("Possible country(es) for $filteredThing")
                    countries.take(5).forEach {
                        addField(
                            it.string("country_name"),
                            "Probability: ${it.double("probability")?.times(100)}%",
                            false
                        )
                    }
                } else {
                    setDescription("Countries not found for $filteredThing")
                }
                setFooter("Powered by gender-api.com")
            }.build()
        )
    }

}