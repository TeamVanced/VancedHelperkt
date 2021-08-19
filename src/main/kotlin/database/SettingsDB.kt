package database

import com.mongodb.client.model.Updates
import config
import database.collections.Settings
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

var muteRoleId: Long = 0L
    get() {
        if (field != 0L) {
            return field
        }

        field = settings.muteRoleId
        return field
    }
    set(value) {
        field = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("muteRoleId", value)
            )
    }