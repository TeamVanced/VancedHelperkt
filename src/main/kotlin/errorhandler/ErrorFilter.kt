package errorhandler

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import ext.sendStacktrace
import jda
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.net.SocketTimeoutException

class ErrorFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent?): FilterReply {
        val throwable = event?.throwableProxy
        if (event?.level == Level.ERROR) {
            jda?.guilds?.forEach {
                if (throwable !is SocketTimeoutException && throwable?.stackTraceElementProxyArray != null) {
                    EmbedBuilder().setColor(Color.red).sendStacktrace(
                        it,
                        throwable.className,
                        throwable.stackTraceElementProxyArray?.joinToString("\n")
                    )
                }
            }
            return FilterReply.ACCEPT
        }
        return FilterReply.NEUTRAL
    }

}