package errorhandler

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import ext.sendStacktrace
import jda

class ErrorFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent?): FilterReply {
        val throwable = event?.throwableProxy
        if (event?.level == Level.ERROR) {
            jda?.guilds?.forEach {
                if (throwable?.className == "java.net.SocketTimeoutException") {
                    return@forEach
                }

                if (throwable?.stackTraceElementProxyArray == null) {
                    return@forEach
                }

                it.sendStacktrace(
                    throwable.className,
                    throwable.stackTraceElementProxyArray?.joinToString("\n")
                )
            }
            return FilterReply.ACCEPT
        }
        return FilterReply.NEUTRAL
    }

}