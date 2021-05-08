package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import config
import ext.hasQuotePerms
import ext.optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import org.koin.core.component.inject
import repository.country.CountryRepositoryImpl
import type.CommandType.Fun

class Country : BaseCommand(
    commandName = "country",
    commandDescription = "Guess the country",
    commandType = Fun,
    commandArguments = mapOf("The thing".optional())
) {

    private val repository by inject<CountryRepositoryImpl>()

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

    private fun TextChannel.detectCountries(name: String) {
        val filteredName = name.filter { it.isLetter() }
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.get(
                token = config.genderToken,
                name = filteredName
            )
            sendMsg(
                embedBuilder.apply {
                    setTitle("Country Detector")
                    if (response.countries.isNotEmpty()) {
                        setDescription("Possible country(es) for $filteredName")
                        response.countries.take(5).forEach {
                            addField(
                                it.countryName,
                                "Probability: ${it.probability}%",
                                false
                            )
                        }
                    } else {
                        setDescription("Countries not found for $filteredName")
                    }
                    setFooter("Powered by gender-api.com")
                }.build()
            )
        }
    }

}