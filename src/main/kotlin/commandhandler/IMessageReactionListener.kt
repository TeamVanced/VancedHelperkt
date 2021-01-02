package commandhandler

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent

interface IMessageReactionListener {

    fun onReactionAdd(event: MessageReactionAddEvent)

    fun onReactionRemove(event: MessageReactionRemoveEvent)

}