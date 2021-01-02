package ext

import net.dv8tion.jda.api.entities.User

fun User.getModLogInfo(): String {
    return "Mention: $asMention\n" +
    "Tag: $asTag\n" +
    "ID: $id"
}