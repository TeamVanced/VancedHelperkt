package core.util

import dev.kord.core.any
import dev.kord.core.entity.Guild
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import org.slf4j.Logger
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
tailrec suspend fun cleanupCcRoles(
    guild: Guild,
    logger: Logger,
) {
    guild.roles.filter { it.name.endsWith("-CC") }.collect { role ->
        val members = guild.members.filter { member ->
            member.roles.any { it == role }
        }
        if (members.count() == 0) {
            logger.info("CC Role cleanup: Deleting ${role.name}")
            role.delete("CC Role cleanup")
        }
    }
    delay(Duration.hours(1))
    cleanupCcRoles(guild, logger)
}