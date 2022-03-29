package core.command.base

import core.command.CommandContext
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.SelectMenuInteraction
import org.koin.core.component.KoinComponent

abstract class BaseCommand(
    val commandName: String,
    val commandDescription: String,
    val defaultPermissions: Boolean = true
) : KoinComponent {

    var commandId: Snowflake? = null

    open suspend fun preInit() = Unit

    abstract suspend fun execute(
        ctx: CommandContext
    )

    open suspend fun onSelectMenuInteraction(
        interaction: SelectMenuInteraction
    ) = Unit

    open suspend fun onButtonInteraction(
        interaction: ButtonInteraction
    ) {
        if (interaction.componentId == "${commandName}-delete") {
            interaction.deferPublicMessageUpdate().delete()
        }
    }

    open suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder()

    open fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder()

}