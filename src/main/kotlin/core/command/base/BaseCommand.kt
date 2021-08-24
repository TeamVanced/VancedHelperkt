package core.command.base

import core.command.CommandContext
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import dev.kord.common.annotation.KordPreview
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

    open suspend fun onSelectMenuInteraction(
        interaction: SelectMenuInteraction
    ) = Unit

    open suspend fun onButtonInteraction(
        interaction: ButtonInteraction
    ) {
        if (interaction.componentId == "${commandName}-delete") {
            interaction.acknowledgePublicDeferredMessageUpdate().delete()
        }
    }

    open suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder()
}