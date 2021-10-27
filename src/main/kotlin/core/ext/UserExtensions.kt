package core.ext

import core.util.botOwners
import dev.kord.core.entity.User

val User.isDev
    get() = botOwners.contains(id.value)
