package core.listener

import core.ext.isMod
import dev.kord.core.entity.Member
import dev.kord.core.firstOrNull

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
        member: Member
    ) {
        member.roles.firstOrNull {
            it.name.endsWith("-CC")
        }?.delete("Role owner left the guild")
    }

}