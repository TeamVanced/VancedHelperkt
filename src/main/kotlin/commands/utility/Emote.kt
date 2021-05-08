package commands.utility

import commandhandler.CommandContext
import commands.base.BaseCommand
import ext.required
import ext.useArguments
import type.CommandType.Utility

class Emote : BaseCommand(
    commandName = "emote",
    commandDescription = "Get a corresponding emote link",
    commandType = Utility,
    commandArguments = mapOf("emotes".required()),
    commandAliases = listOf("emoji", "e")
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
                    val link = "<https://cdn.discordapp.com/emojis/${emote.substringAfterLast(":").dropLast(1)}.$suffix>"
                    if (emotes.size == 1) {
                        emotelinks.add(link.removePrefix("<").removeSuffix(">"))
                    } else {
                        emotelinks.add(link)
                    }
                } else {
                    emotelinks.add("Not an emote")
                }
            }
            ctx.event.channel.sendMsg(emotelinks.distinct().joinToString("\n"))
        } else {
            ctx.channel.useArguments(1)
        }
    }

}
