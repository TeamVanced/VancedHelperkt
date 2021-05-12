package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import config
import ext.hasQuotePerms
import ext.optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.Message
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
            ctx.message.replyMsg("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            if (args[0].contains(contentIDRegex)) {
                ctx.guild.retrieveMemberById(contentIDRegex.find(args[0])!!.value).queue({
                    ctx.message.detectCountries(it.user.name.substringBefore(" "))
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER) {
                    ctx.message.replyMsg("Provided member does not exist!")
                }.handle(ErrorResponse.UNKNOWN_USER) {
                    ctx.message.replyMsg("Provided user does not exist!")
                })
            } else {
                ctx.message.detectCountries(args.joinToString(" "))
            }
        } else {
            ctx.message.detectCountries(event.author.name)
        }

    }

    private fun Message.detectCountries(name: String) {
        val filteredName = name.filter { it.isLetter() }
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.get(
                token = config.genderToken,
                name = filteredName
            )
            replyMsg(
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