package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced
import net.dv8tion.jda.api.entities.MessageEmbed

class Info : BasePagerCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val jsonName: String
        get() = "faq"

}