package core.ext

import database.whitelistedSpamChannelIds
import dev.kord.core.entity.channel.MessageChannel

val MessageChannel.isWhitelistedSpamChannel: Boolean
    get() = whitelistedSpamChannelIds.contains(id.value)