package application

/**
 * @param name 3-32 character name matching ^[\w-]{3,32}$ regex
 * @param description
 */

data class ApplicationCommand(
    val name: String,
    val description: String,
    val optionApplications: List<ApplicationCommandOption> = listOf()
)