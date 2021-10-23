package core.listener

import dev.kord.common.entity.Snowflake
import dev.kord.core.any
import dev.kord.core.behavior.edit
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
        val ccRole = member.roles.firstOrNull { it.name.endsWith("-CC") }
        if (ccRole != null) {
            member.edit {
                roles?.remove(ccRole.id)
            }
        }
    }

}