package core.listener

import core.ext.isMod
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.coroutines.flow.filter
import org.slf4j.Logger

class UserListener {

    suspend fun onMemberUnboostGuild(
        member: Member,
        logger: Logger,
    ) {
        if (member.isMod) return

        member.roles.filter { role ->
            role.name.endsWith("-CC")
        }.collect { role ->
            logger.info("Role owner unboosted the server, deleting `${role.name}` role")
            role.delete("Role owner unboosted the server")
        }
    }

    // Switch to this if older overload doesn't work
//    suspend fun onMemberLeaveGuild(
//        guild: Guild,
//        logger: Logger,
//    ) {
//        val guildMembers = guild.members
//        guild.roles.filter {
//            it.name.endsWith("-CC")
//        }.filterNot { role ->
//            guildMembers.any { member ->
//                member.roles.any { it == role }
//            }
//        }.collect { role ->
//            logger.info("CC Role cleanup: Deleting ${role.name}")
//            role.delete("CC Role cleanup")
//        }
//    }

    suspend fun onMemberLeaveGuild(
        oldMember: Member,
        logger: Logger
    ) {
        oldMember.roles.filter { role ->
            role.name.endsWith("-CC")
        }.collect { role ->
            logger.info("Role owner left the server, deleting ${role.name}")
            role.delete("Role owner left the server")
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

    suspend fun onMemberUnban(member: User) {
        sendInfractionToModLogChannel(
            Infraction.Unban(member)
        )
    }

}