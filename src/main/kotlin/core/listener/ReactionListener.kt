package core.listener

import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.rest.builder.message.create.actionRow

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