package commands.vanced

import commands.base.BaseGuideCommand
import type.CommandType.Vanced

class Troubleshoot : BaseGuideCommand(
    commandName = "troubleshoot",
    commandDescription = "Troubleshoot Vanced issues. For your own issues, consult your therapist ;)",
    commandType = Vanced,
    commandAliases = listOf("ts"),
    jsonName = "troubleshooting"
)