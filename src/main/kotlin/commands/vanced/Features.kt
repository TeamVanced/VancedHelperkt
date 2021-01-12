package commands.vanced

import commands.BasePagerCommand
import commands.CommandType.Vanced

class Features : BasePagerCommand(
    commandName = "features",
    commandDescription = "Get more info about vanced features",
    commandType = Vanced
) {

    override val jsonName: String
        get() = "features"

}