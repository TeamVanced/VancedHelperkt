package slashcommands

import slashcommands.application.ApplicationCommand
import slashcommands.application.ISlashCommandListener

class DSKBuilder(private val token: String) {

    private var commandListener: ISlashCommandListener? = null
    private var globalCommands = mutableListOf<ApplicationCommand>()
    private var guildCommands = mutableMapOf<String, List<ApplicationCommand>>()

    fun setSlashCommandListener(listener: ISlashCommandListener) {
        commandListener = listener
    }

    fun registerGlobalCommands(vararg applicationCommands: ApplicationCommand) {
        applicationCommands.forEach {
            globalCommands.add(it)
        }
    }

    fun registerGuildCommands(guildId: String, vararg applicationCommands: ApplicationCommand) {
        guildCommands[guildId] = applicationCommands.toList()
    }

    fun build() {



    }

}