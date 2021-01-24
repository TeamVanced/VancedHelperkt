import application.ApplicationCommand
import application.ISlashCommandListener

class DSKBuilder {

    private var botToken: String? = null
    private var commandListener: ISlashCommandListener? = null
    private var globalCommands = mutableListOf<ApplicationCommand>()
    private var guildCommands = mutableMapOf<String, List<ApplicationCommand>>()

    fun setToken(token: String) {
        botToken = token
    }

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
        if (botToken == null) {
            throw IllegalArgumentException("Token should not be null!")
        }

    }

}