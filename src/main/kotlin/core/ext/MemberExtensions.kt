package core.ext

import core.database.allowedQuoteRoleIds
import core.database.deleteUserWarns
import core.database.getUserWarns
import core.database.moderatorRoleIds
import core.util.botOwners
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member

suspend fun Member.checkWarnForTooManyInfractions() {
    val userId = id.asString
    val warns = getUserWarns(userId)

    if (warns != null && warns.reasons.size >= 3) {
        kick("Too many infractions")
        deleteUserWarns(userId)
    }
}

val Member.isMod
    get() = moderatorRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value
        }.contains(modRoleId)
    }

val Member.isQuoter
    get() = allowedQuoteRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value
        }.contains(modRoleId)
    }

val Member.isDev
    get() = botOwners.contains(id.value)