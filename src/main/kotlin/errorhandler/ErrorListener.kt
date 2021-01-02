package errorhandler

import ext.sendStacktrace
import jda
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.ExceptionEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.exceptions.ContextException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class ErrorListener : ListenerAdapter() {

    private val embedBuilder = EmbedBuilder().setColor(Color.red)

    override fun onException(event: ExceptionEvent) {
        jda?.guilds?.forEach {
            embedBuilder.sendStacktrace(it, event.cause.message, event.cause.stackTraceToString())
        }
    }

    override fun onGenericEvent(event: GenericEvent) {
        if (event is ContextException) {
            jda?.guilds?.forEach {
                embedBuilder.sendStacktrace(it, event.cause?.message, event.cause?.stackTraceToString())
            }
        }
    }

}