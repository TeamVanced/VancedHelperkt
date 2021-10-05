package core.database

import config
import core.database.data.MongoItemParameter
import core.database.data.MongoListParameter
import core.database.data.mongoItem
import core.database.data.mongoMutableListOf
import core.database.collections.Settings
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val settingsCollection = helperDB.getCollection<Settings>("settings")

val settings: Settings
    get() {
        val foundCollection = settingsCollection.findOne(guildDBObject)
        if (foundCollection != null) {
            return foundCollection
        }

        settingsCollection.insertOne(Settings(config.guildId))
        return settingsCollection.findOne(guildDBObject)!!
    }

val whitelistedSpamChannelIds = mongoMutableListOf(
    MongoListParameter(
        name = "whitelistedSpamChannelIds",
        list = settings.whitelistedSpamChannelIds,
        collection = settingsCollection,
    )
)

val moderatorRoleIds = mongoMutableListOf(
    MongoListParameter(
        name = "modRoleIds",
        list = settings.modRoleIds,
        collection = settingsCollection,
    )
)

val allowedQuoteRoleIds = mongoMutableListOf(
    MongoListParameter(
        name = "allowedQuoteRoleIds",
        list = settings.allowedQuoteRoleIds,
        collection = settingsCollection,
    )
)

val allowedColourMeRoleIds = mongoMutableListOf(
    MongoListParameter(
        name = "allowedColourMeRoleIds",
        list = settings.allowedColourMeRoleIds,
        collection = settingsCollection,
    )
)

var muteRoleId by mongoItem(
    MongoItemParameter(
        name = "muteRoleId",
        item = settings.muteRoleId,
        collection = settingsCollection
    )
)

var boosterRoleId by mongoItem(
    MongoItemParameter(
        name = "boosterRoleId",
        item = settings.boosterRoleId,
        collection = settingsCollection
    )
)

var logChannelId by mongoItem(
    MongoItemParameter(
        name = "logChannelId",
        item = settings.logChannelId,
        collection = settingsCollection
    )
)

var modLogChannelId by mongoItem(
    MongoItemParameter(
        name = "modLogChannelId",
        item = settings.modLogChannelId,
        collection = settingsCollection
    )
)

var errorChannelId by mongoItem(
    MongoItemParameter(
        name = "errorChannelId",
        item = settings.errorChannelId,
        collection = settingsCollection
    )
)