package application

interface ISlashCommandListener {

    fun onEvent(event: SlashCommandReceivedEvent)

}