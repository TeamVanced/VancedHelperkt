package commands.vanced

import commands.base.BaseArrowpageCommand
import type.CommandType.Vanced

class Troubleshoot : BaseArrowpageCommand(
    commandName = "troubleshoot",
    commandDescription = "Troubleshoot Vanced issues. For your own issues, consult your therapist ;)",
    commandType = Vanced,
    commandAliases = listOf("ts")
) {

    override val jsonName: String
        get() = "troubleshooting"

}