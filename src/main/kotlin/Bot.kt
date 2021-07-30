import core.command.CommandManager
import core.command.base.BaseCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.kordLogger
import dev.kord.core.on
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.reflections.Reflections
import org.slf4j.LoggerFactory

class Bot : KoinComponent {

    private val commandManager: CommandManager by inject()

    private val logger = LoggerFactory.getLogger("Vanced Helper")

    @OptIn(KordPreview::class)
    suspend fun start() {
        val kord = Kord(config.token)

        val commands = Reflections("commands")
            .getSubTypesOf(BaseCommand::class.java)
            .map { it.getConstructor().newInstance() }

        commands.forEach { command ->
            commandManager.addCommand(command)
            with(command) {
                logger.info("Registering command: ${command.name}")
                kord.registerCommand()
            }
        }

        kord.on<InteractionCreateEvent> {
            commandManager.respond(interaction)
        }

        kord.login()
    }

}