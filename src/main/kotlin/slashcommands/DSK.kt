package slashcommands

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import slashcommands.application.ISlashCommandListener
import slashcommands.application.SlashCommandReceivedEvent
import slashcommands.interaction.Interaction
import slashcommands.interaction.InteractionType

class DSK (
    private val applicationId: String,
    private val token: String,
    private val listener: ISlashCommandListener,
) {

    //TODO
    init {
        embeddedServer(Netty, 8080) {
            routing {
                route("/", HttpMethod.Post) {
                    get {
                        val response = call.receive<Interaction>()
                        if (response.type == InteractionType.Ping) {
                            call.respondText(
                                """
                                    "type": 1
                                """.trimIndent()
                            )

                        } else {
                            listener.onEvent(SlashCommandReceivedEvent(response))
                        }
                    }
                }
            }
        }
    }

}