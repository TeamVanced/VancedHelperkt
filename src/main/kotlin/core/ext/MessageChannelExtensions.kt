package core.ext

import core.database.whitelistedAutoresponsesChannelIds
import core.database.whitelistedSpamChannelIds
import dev.kord.core.entity.channel.MessageChannel

val MessageChannel.isWhitelistedSpamChannel: Boolean
    get() = id.value.toLong() in whitelistedSpamChannelIds

val MessageChannel.isWhitelistedAutoresponseChannel: Boolean
    get() = id.value.toLong() in whitelistedAutoresponsesChannelIds