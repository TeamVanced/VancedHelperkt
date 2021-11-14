package core.listener

import core.ext.isMod
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import dev.kord.core.any
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.kord.core.firstOrNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import org.slf4j.Logger

class UserListener {

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

    suspend fun onMemberKick(
        member: User,
        moderator: User,
        reason: String?
    ) {
        sendInfractionToModLogChannel(
            Infraction.Kick(member, moderator, reason)
        )
    }

    suspend fun onMemberBan(
        member: User,
        moderator: User,
        reason: String?
    ) {
        sendInfractionToModLogChannel(
            Infraction.Ban(member, moderator, reason)
        )
    }

    suspend fun onMemberUnban(
        member: User,
    ) {
        sendInfractionToModLogChannel(
            Infraction.Unban(member)
        )
    }

}