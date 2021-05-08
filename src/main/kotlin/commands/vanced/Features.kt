package commands.vanced

import commands.base.BaseGuideCommand
import type.CommandType.Vanced

class Features : BaseGuideCommand(
    commandName = "features",
    commandDescription = "Get more info about vanced features",
    commandType = Vanced
) {

    override val jsonName: String
        get() = "features"

}