package core.ext

import core.database.allowedQuoteRoleIds
import core.database.deleteUserWarns
import core.database.getUserWarns
import core.database.moderatorRoleIds
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import dev.kord.core.entity.Member
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

suspend fun Member.checkWarnForTooManyInfractions() {
    val userId = id.toString()
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

suspend fun Member.canInteractWith(target: Member): Boolean {
    val issuer = this

    val issuerRoles = issuer.roles.sortedByDescending { it.rawPosition }
    val targetRoles = target.roles.sortedByDescending { it.rawPosition }

    if (issuer.isMod && target.isMod) {
        val issuerHighestModeratorRolePosition = issuerRoles.first {
            moderatorRoleIds.contains(it.id.value.toLong())
        }.getPosition()

        val targetHighestModeratorRolePosition = targetRoles.first {
            moderatorRoleIds.contains(it.id.value.toLong())
        }.getPosition()


        return issuerHighestModeratorRolePosition > targetHighestModeratorRolePosition
    }

    val issuerHighestRolePosition = issuerRoles.firstOrNull()?.getPosition() ?: 0
    val targetHighestRolePosition = targetRoles.firstOrNull()?.getPosition() ?: 0

    return issuerHighestRolePosition > targetHighestRolePosition
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