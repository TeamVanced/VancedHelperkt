package commands.vanced

import commands.BasePagerCommand
import commands.CommandType.Vanced

class Info : BasePagerCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val jsonName: String
        get() = "faq"

}