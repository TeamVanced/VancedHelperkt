package ext

import database.cachedModeratorRoleIds
import database.deleteUserWarns
import database.getUserWarns
import dev.kord.core.entity.Member

suspend fun Member.checkWarnForTooManyInfractions() {
    val userId = id.asString
    val warns = getUserWarns(userId)

    if (warns != null && warns.reasons.size >= 3) {
        kick("Too many infractions")
        deleteUserWarns(userId)
    }
}

val Member.isMod get() = cachedModeratorRoleIds.any { modRoleId ->
    roleIds.map { roleId ->
        roleId.value
    }.contains(modRoleId)
}