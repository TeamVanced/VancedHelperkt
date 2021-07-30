package core.command.base

import config
import core.command.CommandContext
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import org.koin.core.component.KoinComponent

@OptIn(KordPreview::class)
abstract class BaseCommand(
    val name: String,
    val description: String,
) : KoinComponent {

    open suspend fun preInit() = Unit

    abstract suspend fun execute(
        ctx: CommandContext
    )

    abstract suspend fun commandOptions(): CustomApplicationCommandCreateBuilder

    suspend fun Kord.registerCommand() {
        slashCommands.createGuildApplicationCommand(
            guildId = Snowflake(config.guildId),
            name = name,
            description = description
        ) {
            with (commandOptions()) {
                arguments()

                boolean(
                    name = "send",
                    description = "Whether to make the output visible to others"
                ) {
                    required = false
                }
            }
        }
    }
}