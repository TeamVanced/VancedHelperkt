package ext

import database.cachedWhitelistedSpamChannelIds
import dev.kord.core.entity.channel.MessageChannel

val MessageChannel.isWhitelistedSpamChannel: Boolean
    get() = cachedWhitelistedSpamChannelIds.contains(id.value)