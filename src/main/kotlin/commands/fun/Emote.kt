package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Utility
import ext.required
import ext.useArguments

class Emote : BaseCommand(
    commandName = "emote",
    commandDescription = "Get a corresponding emote link",
    commandType = Utility,
    commandArguments = mapOf("emotes".required()),
    commandAliases = listOf("e")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val emotes = ctx.args
        val regex = "<?(a)?:?(\\w{2,32}):(\\d{17,19})>?".toRegex()
        val emotelinks = mutableListOf<String>()
        if (emotes.isNotEmpty()) {
            emotes.forEach { emote ->
                val suffix = if (emote.startsWith("<a")) "gif" else "png"
                if (emote.matches(regex)) {
                    val link = "<https://cdn.discordapp.com/emojis/${emote.filter { it.isDigit() }}.$suffix?size=64>"
                    if (emotes.size == 1) {
                        emotelinks.add(link.removePrefix("<").removeSuffix(">"))
                    } else {
                        emotelinks.add(link)
                    }
                } else {
                    emotelinks.add("Not an emote")
                }
            }
            sendMessage(emotelinks.distinct().joinToString("\n"))
        } else {
            useArguments(1)
        }
    }

}