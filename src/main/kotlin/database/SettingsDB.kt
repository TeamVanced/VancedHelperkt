package database

import config

import com.mongodb.client.model.Updates
import database.collections.Settings
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val settingsCollection = helperDB.getCollection<Settings>("settings")

private var _muteRoleId: Long = 0

val settings: Settings
    get() {
        val foundCollection = settingsCollection.findOne(guildDBObject)
        if (foundCollection != null) {
            println("got")
            return foundCollection
        }

        println("inited")
        settingsCollection.insertOne(Settings(config.guildId))
        return settingsCollection.findOne(guildDBObject)!!
    }

var muteRoleId: Long
    get() {
        if (_muteRoleId != 0L) {
            return _muteRoleId
        }

        _muteRoleId = settings.muteRoleId
        return _muteRoleId
    }
    set(value) {
        _muteRoleId = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("muteRoleId", value)
            )
    }