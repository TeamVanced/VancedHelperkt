package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import config
import ext.hasQuotePerms
import ext.optional
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import utils.getJson

class Gender : BaseCommand(
    commandName = "gender",
    commandDescription = "Guess the gender",
    commandType = Fun,
    commandArguments = mapOf("The thing".optional()),
    commandAliases = listOf("sex")
) {

    private val baseUrl = "https://gender-api.com/get?key=${config.genderToken}"

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
                    detectGender(it.user.name)
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER) {
                    sendMessage("Provided member does not exist!")
                }.handle(ErrorResponse.UNKNOWN_USER) {
                    sendMessage("Provided user does not exist!")
                })
            } else {
                detectGender(args.joinToString(" "))
            }
        } else {
            detectGender(event.author.name)
        }

    }

    private fun detectGender(thing: String) {
        val filteredThing = thing.filter { it.isLetter() }
        val json = "$baseUrl&email=${filteredThing.replace(" ", ".")}@gmail.com".getJson()
        val gender = json?.string("gender")
        val accuracy = json?.int("accuracy")
        sendMessage(
            embedBuilder.apply {
                setTitle("Gender Detector")
                setDescription("$filteredThing is $gender\nAccuracy: $accuracy%")
                setFooter("Powered by gender-api.com")
            }.build()
        )
    }

}