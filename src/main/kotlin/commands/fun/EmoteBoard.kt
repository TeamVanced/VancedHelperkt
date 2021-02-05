package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import database.collections.Emote
import database.emotesCollection
import ext.optional
import ext.useCommandProperly
import org.litote.kmongo.eq

class EmoteBoard : BaseCommand(
    commandName = "emoteboard",
    commandDescription = "Get most frequently used emotes",
    commandType = Fun,
    commandAliases = listOf("eb"),
    commandArguments = mapOf("least | clean".optional())
) {

    private val Emote.description: String get() = "$emote (Used $usedCount times)\n"

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isEmpty()) {
            val ebemotes: List<Emote> = emotesCollection.find(Emote::guildId eq guildId).filter { it.usedCount > 0 }.sortedByDescending { it.usedCount }.take(10)
            if (ebemotes.isEmpty()) {
                ctx.event.channel.sendMsg("Frequently used emotes not found")
                return
            }
            ctx.event.channel.sendMsg(
                embedBuilder.apply {
                    setTitle("Emoteboard")
                    val description = mutableListOf<String>()
                    for (i in ebemotes.indices) {
                        description.add("${ebemotes[i].emote} (Used ${ebemotes[i].usedCount} times)")
                    }
                    setDescription(description.joinToString("\n"))
                }.build()
            )
        } else {
            when (args[0]) {
                "least" -> {
                    val collection = emotesCollection.find(Emote::guildId eq guildId).sortedBy { it.usedCount }
                    var notUsedCounter = 0
                    val ebemotes = collection.filter { it.usedCount > 0 }.take(10)
                    val totalNotUsed = collection.filter { it.usedCount == 0 }
                    val notUsed = totalNotUsed.takeWhile {
                        notUsedCounter += it.emote.length + 1 // + 1 is for " "
                        notUsedCounter < 1021
                    }
                    ctx.event.channel.sendMsg(
                        embedBuilder.apply {
                            setTitle("Emoteboard")
                            ebemotes.forEach {
                                appendDescription(it.description)
                            }
                            addField(
                                "Not used emotes",
                                notUsed.joinToString(separator = " ") { it.emote }.let { if (notUsed.size < totalNotUsed.size) "$it..." else it },
                                false
                            )
                        }.build()
                    )
                }
                "clean" -> {
                    emotesCollection.deleteMany(Emote::usedCount eq 0)
                    ctx.event.channel.sendMsg("Successfully removed unused emotes from database")
                }
                else -> {
                    ctx.channel.useCommandProperly()
                }
            }
        }
    }

}