package ext

import database.boosterRole
import database.modRoles
import net.dv8tion.jda.api.entities.Member

fun Member.isMod(guildId: String): Boolean {
    val modRoles = guildId.modRoles
    return roles.any { modRoles.contains(it.id) }
}

fun Member.isBooster(guildId: String): Boolean {
    val boosterRole = guildId.boosterRole
    return roles.any { boosterRole == it.id }
}