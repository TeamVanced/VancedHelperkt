package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced

class BugReport : BasePagerCommand(
    commandName = "bugreport",
    commandDescription = "Report Vanced bugs. For broken toasters and such, consult your local Indian tech guru ;P",
    commandType = Vanced,
    commandAliases = listOf("bug", "br")
) {

    override val jsonName: String
        get() = "bugreport"

}