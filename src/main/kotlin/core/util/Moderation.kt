package core.util

import core.database.modLogChannelId
import core.ext.userInfo
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.embed

sealed class Infraction(
    val action: String,
    val user: User,
    val moderator: User?,
    val reason: String?
) {

    class Unban(
        user: User,
    ) : Infraction("User unbanned", user, null, null)

    class Unmute(
        user: User,
        moderator: User,
    ) : Infraction("User unmuted", user, moderator, null)

    class Unwarn(
        user: User,
        moderator: User,
    ) : Infraction("User unwarned", user, moderator, null)

    class Ban(
        user: User,
        moderator: User,
        reason: String?,
    ) : Infraction("User banned", user, moderator, reason)

    class Kick(
        user: User,
        moderator: User,
        reason: String?,
    ) : Infraction("User kicked", user, moderator, reason)

    class Mute(
        user: User,
        moderator: User,
        reason: String?,
    ) : Infraction("User muted", user, moderator, reason)

    class Warn(
        user: User,
        moderator: User,
        reason: String?,
    ) : Infraction("User warned", user, moderator, reason)
}

suspend fun sendInfractionToModLogChannel(
    infraction: Infraction
) {
    val modLogchannel = infraction.user.kord.getChannelOf<TextChannel>(id = Snowflake(modLogChannelId))

    modLogchannel?.createMessage {
        embed {
            color = Color(254, 231, 92)
            title = infraction.action
            field {
                name = "User"
                value = infraction.user.userInfo
            }
            if (infraction.moderator != null) {
                field {
                    name = "Moderator"
                    value = infraction.moderator.userInfo
                }
            }
            if (infraction.reason != null) {
                field {
                    name = "Reason"
                    value = infraction.reason
                }
            }
        }
    }
}