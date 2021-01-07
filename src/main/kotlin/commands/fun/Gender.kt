package commands.`fun`

import config
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Fun
import ext.hasQuotePerms
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import utils.getJson

class Gender : BaseCommand(
    commandName = "gender",
    commandDescription = "Guess the gender",
    commandType = Fun,
    commandArguments = listOf("[The thing]")
) {

    private val baseUrl = "https://gender-api.com/get?key=${config.genderToken}"

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        if (ctx.authorAsMember?.hasQuotePerms(guildId) == false) {
            ctx.channel.sendMessage("You are not allowed to use this command").queueAddReaction()
            return
        }
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            if (args[0].contains(contentIDRegex)) {
                ctx.guild.retrieveMemberById(contentIDRegex.find(args[0])!!.value).queue({
                    detectGender(it.user.name)
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_MEMBER) {
                    channel.sendMessage("Provided member does not exist!").queueAddReaction()
                }.handle(ErrorResponse.UNKNOWN_USER) {
                    channel.sendMessage("Provided user does not exist!").queueAddReaction()
                })
            } else {
                detectGender(args.joinToString(" "))
            }
        } else {
            detectGender(event.author.name)
        }

    }

    private fun detectGender(thing: String) {
        val json = if (thing.contains(" ")) "$baseUrl&email=${thing.replace(" ", ".")}@gmail.com".getJson() else "$baseUrl&name=$thing".getJson()
        val gender = json?.string("gender")
        val accuracy = json?.int("accuracy")
        channel.sendMessage(
            embedBuilder.apply {
                setTitle("Gender Detector")
                setDescription("$thing is $gender\nAccuracy: $accuracy%")
                setFooter("Powered by gender-api.com")
            }.build()
        ).queueAddReaction()
    }

}