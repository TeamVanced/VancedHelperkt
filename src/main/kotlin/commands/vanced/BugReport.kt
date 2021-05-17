package commands.vanced

import commands.base.BaseGuideCommand
import type.CommandType.Vanced

class BugReport : BaseGuideCommand(
    commandName = "bugreport",
    commandDescription = "Report Vanced bugs. For broken toasters and such, consult your local Indian tech guru ;P",
    commandType = Vanced,
    commandAliases = listOf("bug", "br"),
    jsonName = "bugreport"
)