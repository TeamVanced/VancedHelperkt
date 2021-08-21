package core.command.base

import config
import core.command.CommandContext
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.SelectMenuInteraction
import org.koin.core.component.KoinComponent

@OptIn(KordPreview::class)
abstract class BaseCommand(
    val commandName: String,
    val commandDescription: String,
) : KoinComponent {

    open suspend fun preInit() = Unit

    abstract suspend fun execute(
        ctx: CommandContext
    )

    open suspend fun onSelectMenu(interaction: SelectMenuInteraction) = Unit

    open suspend fun onSelectButton(interaction: ButtonInteraction) = Unit

    open suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder()

    suspend fun Kord.registerCommand() {
        slashCommands.createGuildApplicationCommand(
            guildId = Snowflake(config.guildId),
            name = commandName,
            description = commandDescription
        ) {
            commandOptions().arguments(this)
        }
    }
}