package core.listener

import core.ext.isMod
import dev.kord.core.any
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.firstOrNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import org.slf4j.Logger

class UserListener {

    suspend fun onMemberBoostGuild(
        member: Member
    ) {

    }

    suspend fun onMemberUnboostGuild(
        member: Member
    ) {
        if (member.isMod) return

        member.roles.firstOrNull {
            it.name.endsWith("-CC")
        }?.delete("Role owner unboosted the guild")
    }

    suspend fun onMemberLeaveGuild(
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
    }

}