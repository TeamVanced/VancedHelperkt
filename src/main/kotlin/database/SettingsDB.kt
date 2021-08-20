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
        if (field == 0L) {
            field = settings.muteRoleId
        }

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

var boosterRoleId: Long = 0L
    get() {
        if (field == 0L) {
            field = settings.boosterRoleId
        }

        return field
    }
    set(value) {
        field = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("boosterRoleId", value)
            )
    }

var logChannelId: Long = 0L
    get() {
        if (field == 0L) {
            field = settings.logChannelId
        }

        return field
    }
    set(value) {
        field = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("logChannelId", value)
            )
    }

var modLogChannelId: Long = 0L
    get() {
        if (field == 0L) {
            field = settings.modLogChannelId
        }

        return field
    }
    set(value) {
        field = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("modLogChannelId", value)
            )
    }

var errorChannelId: Long = 0L
    get() {
        if (field == 0L) {
            field = settings.errorChannelId
        }

        return field
    }
    set(value) {
        field = value
        settingsCollection
            .findOneAndUpdate(
                guildDBObject,
                Updates.set("errorChannelId", value)
            )
    }

fun addModeratorRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.push("modRoleIds", roleId)
    )
}

fun removeModeratorRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.pull("modRoleIds", roleId)
    )
}

fun addAllowedQuoteRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.push("allowedQuoteRoleIds", roleId)
    )
}

fun removeAllowedQuoteRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.pull("allowedQuoteRoleIds", roleId)
    )
}

fun addAllowedColourMeRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.push("allowedColourMeRoleIds", roleId)
    )
}

fun removeAllowedColourMeRoleId(
    roleId: Long
) {
    settingsCollection.findOneAndUpdate(
        guildDBObject,
        Updates.pull("allowedColourMeRoleIds", roleId)
    )
}