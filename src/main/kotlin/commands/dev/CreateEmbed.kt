package commands.dev

import com.beust.klaxon.Klaxon
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Dev
import ext.useCommandProperly
import java.awt.Color

class CreateEmbed : BaseCommand(
    commandName = "createembed",
    commandDescription = "Create an embed from a provided json",
    commandType = Dev,
    commandArguments = listOf("<json>"),
    commandAliases = listOf("embed"),
    devOnly = true
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val json = args.joinToString(" ")
            channel.sendMessage(
                embedBuilder.apply {
                    try {
                        with(Klaxon().parse<JsonEmbed>(json)) {
                            setTitle(this?.title)
                            setDescription(this?.description)
                            if (this?.fields != null) {
                                this.fields.forEach {
                                    addField(
                                        it.title,
                                        it.content,
                                        false
                                    )
                                }
                            }
                            setImage(this?.image)
                            setThumbnail(this?.thumbnail)
                            setFooter(this?.footer)

                            if (this?.color != null) {
                                setColor(Color.decode(this.color))
                            }
                        }
                    } catch (e: Exception) {
                        channel.sendMessage("Could not create an embed").queueAddReaction()
                        return
                    }
                }.build()
            ).queueAddReaction()
        } else {
            useCommandProperly()
        }
    }

}