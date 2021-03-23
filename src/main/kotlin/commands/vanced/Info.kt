package commands.vanced

import commands.BaseArrowpageCommand
import commands.CommandType.Vanced

class Info : BaseArrowpageCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val jsonName: String
        get() = "faq"

}