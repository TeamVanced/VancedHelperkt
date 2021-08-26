package core.listener

import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
class ReactionListener {

    suspend fun grantEmoteRole(
        emoji: ReactionEmoji.Custom,
        message: Message
    ) {

    }

    suspend fun removeEmoteRole(
        emoji: ReactionEmoji.Custom,
        message: Message
    ) {

    }

}