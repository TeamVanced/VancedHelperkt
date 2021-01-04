package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced

class Troubleshoot : BasePagerCommand(
    commandName = "troubleshoot",
    commandDescription = "Troubleshoot Vanced issues. For your own issues, consult your therapist ;)",
    commandType = Vanced,
    commandAliases = listOf("ts")
) {

    override val jsonName: String
        get() = "troubleshooting"

}