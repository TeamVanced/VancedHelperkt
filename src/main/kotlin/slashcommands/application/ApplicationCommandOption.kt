package slashcommands.application

data class ApplicationCommandOption(
    val type: ApplicationCommandOptionType,
    val name: String,
    val description: String,
    val default: Boolean = false,
    val required: Boolean = true,
    val choices: List<ApplicationCommandOptionChoice> = listOf(),
    val options: List<ApplicationCommandOption> = listOf()
)
