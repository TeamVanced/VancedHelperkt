package slashcommands.application

interface ISlashCommandListener {

    fun onEvent(event: SlashCommandReceivedEvent)

}