package commands.vanced

import commands.base.BaseArrowpageCommand
import type.CommandType.Vanced

class Features : BaseArrowpageCommand(
    commandName = "features",
    commandDescription = "Get more info about vanced features",
    commandType = Vanced
) {

    override val jsonName: String
        get() = "features"

}