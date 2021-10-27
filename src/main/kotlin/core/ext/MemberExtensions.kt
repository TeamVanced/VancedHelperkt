package core.ext

import core.database.allowedQuoteRoleIds
import core.database.deleteUserWarns
import core.database.getUserWarns
import core.database.moderatorRoleIds
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import dev.kord.core.entity.Member

suspend fun Member.checkWarnForTooManyInfractions() {
    val userId = id.asString
    val warns = getUserWarns(userId)

    if (warns != null && warns.reasons.size >= 3) {
        val reason = "Too many infractions"
        kick(reason)
        sendInfractionToModLogChannel(
            Infraction.Kick(this, kord.getSelf(), reason)
        )
        deleteUserWarns(userId)
    }
}

val Member.isMod
    get() = moderatorRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value.toLong()
        }.contains(modRoleId)
    }

val Member.isQuoter
    get() = allowedQuoteRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value.toLong()
        }.contains(modRoleId)
    }