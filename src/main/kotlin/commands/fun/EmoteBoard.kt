package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Quotes
import database.collections.Emote
import database.emotesCollection

class EmoteBoard : BaseCommand(
    commandName = "emoteboard",
    commandDescription = "Get most frequently used emotes",
    commandType = Quotes,
    commandAliases = listOf("eb"),
    addTrashCan = false
) {

    private var ebemotes = emptyList<Emote>()

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        ebemotes = emotesCollection.find().sortedByDescending { it.usedCount }.take(10)
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
    }

}