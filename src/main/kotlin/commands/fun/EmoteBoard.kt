package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Quotes
import database.collections.Emote
import database.emotesCollection
import ext.useCommandProperly
import org.litote.kmongo.eq

class EmoteBoard : BaseCommand(
    commandName = "emoteboard",
    commandDescription = "Get most frequently used emotes",
    commandType = Quotes,
    commandAliases = listOf("eb"),
    addTrashCan = false
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isEmpty()) {
            val ebemotes: List<Emote> = emotesCollection.find(Emote::guildId eq guildId).filter { it.usedCount > 0 }.sortedByDescending { it.usedCount }.take(10)
            if (ebemotes.isEmpty()) {
                ctx.event.channel.sendMessage("Frequently used emotes not found").queueAddReaction()
                return
            }
            channel.sendMessage(
                embedBuilder.apply {
                    setTitle("Emoteboard")
                    val description = mutableListOf<String>()
                    for (i in ebemotes.indices) {
                        description.add("${ebemotes[i].emote} (Used ${ebemotes[i].usedCount} times)")
                    }
                    setDescription(description.joinToString("\n"))
                }.build()
            ).queueAddReaction()
        } else {
            if (args[0] == "least") {
                val ebemotes = emotesCollection.find(Emote::guildId eq guildId).sortedBy { it.usedCount }
                channel.sendMessage(
                    embedBuilder.apply {
                        setTitle("Emoteboard")
                        val description = mutableListOf<String>()
                        val notUsedEmotes = mutableListOf<String>()
                        for (i in ebemotes.indices) {
                            val emote = ebemotes[i]
                            if (emote.usedCount == 0) {
                                notUsedEmotes.add(emote.emote)
                            } else {
                                description.add("${ebemotes[i].emote} (Used ${ebemotes[i].usedCount} times)")
                            }
                        }
                        setDescription(notUsedEmotes.joinToString() + " (Used 0 times)\n" + description.joinToString("\n"))
                    }.build()
                ).queueAddReaction()
            } else {
                useCommandProperly()
            }
        }
    }

}