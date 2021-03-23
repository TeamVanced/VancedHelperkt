package commands.vanced

import commands.BaseArrowpageCommand
import commands.CommandType.Vanced

class BugReport : BaseArrowpageCommand(
    commandName = "bugreport",
    commandDescription = "Report Vanced bugs. For broken toasters and such, consult your local Indian tech guru ;P",
    commandType = Vanced,
    commandAliases = listOf("bug", "br")
) {

    override val jsonName: String
        get() = "bugreport"

}