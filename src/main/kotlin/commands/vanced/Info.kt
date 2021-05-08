package commands.vanced

import commands.base.BaseArrowpageCommand
import type.CommandType.Vanced

class Info : BaseArrowpageCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val jsonName: String
        get() = "faq"

}