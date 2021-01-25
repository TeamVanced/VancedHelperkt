package slashcommands.interaction

enum class InteractionResponseType(val value: Int) {

    Pong(1),
    Acknowledge(2),
    ChannelMessage(3),
    ChannelMessageWithSource(4),
    AcknowledgeWithSource(5)

}