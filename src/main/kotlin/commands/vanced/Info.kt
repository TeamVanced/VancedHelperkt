package commands.vanced

import commands.base.BaseGuideCommand
import type.CommandType.Vanced

class Info : BaseGuideCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val jsonName: String
        get() = "faq"

}