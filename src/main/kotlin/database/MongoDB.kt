package database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Updates
import config
import database.collections.Emote
import database.collections.Quote
import database.collections.Settings
import database.collections.Warn
import defaultPrefix
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val client: MongoClient = KMongo.createClient(config.mongoString)
val helperDB: MongoDatabase = client.getDatabase("VancedHelper")
val quotesCollection = helperDB.getCollection<Quote>("quotes")
val settingsCollection = helperDB.getCollection<Settings>()
val warnsCollection = helperDB.getCollection<Warn>("warns")
val emotesCollection = helperDB.getCollection<Emote>("emotes")

val String.settings: Settings?
    get() {
        return try {
            settingsCollection.findOne(Settings::guildId eq this)
        } catch (e: Exception) {
            null
        }
    }

var String.prefix: String
    get() = settings?.prefix ?: defaultPrefix
    set(value) {
        if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.set("prefix", value)) == null) {
            settingsCollection.insertOne(
                Settings(
                    prefix = value,
                    guildId = this
                )
            )
        }
    }

var String.logChannel: String
    get() = settings?.logChannel ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.set("logChannel", value)) == null) {
            settingsCollection.insertOne(
                Settings(
                    errorChannel = value,
                    guildId = this
                )
            )
        }
    }

var String.modlogChannel: String
    get() = settings?.modlogChannel ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(
                Settings::guildId eq this,
                Updates.set("modlogChannel", value)
            ) == null
        ) {
            settingsCollection.insertOne(
                Settings(
                    errorChannel = value,
                    guildId = this
                )
            )
        }
    }

var String.boosterChannel: String
    get() = settings?.boosterChannel ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(
                Settings::guildId eq this,
                Updates.set("boosterChannel", value)
            ) == null
        ) {
            settingsCollection.insertOne(
                Settings(
                    boosterChannel = value,
                    guildId = this
                )
            )
        }
    }

var String.errorChannel: String
    get() = settings?.errorChannel ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(
                Settings::guildId eq this,
                Updates.set("errorChannel", value)
            ) == null
        ) {
            settingsCollection.insertOne(
                Settings(
                    errorChannel = value,
                    guildId = this
                )
            )
        }
    }

var String.muteRole: String
    get() = settings?.muteRole ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.set("muteRole", value)) == null) {
            settingsCollection.insertOne(
                Settings(
                    muteRole = value,
                    guildId = this
                )
            )
        }
    }

var String.boosterChat: String
    get() = settings?.boosterChat ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.set("boosterChat", value)) == null) {
            settingsCollection.insertOne(
                Settings(
                    boosterChat = value,
                    guildId = this
                )
            )
        }
    }

var String.boosterRole: String
    get() = settings?.boosterRole ?: ""
    set(value) {
        if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.set("boosterRole", value)) == null) {
            settingsCollection.insertOne(
                Settings(
                    boosterRole = value,
                    guildId = this
                )
            )
        }
    }

val String.quoteRoles: List<String>
    get() = settings?.allowedQuoteRoles ?: emptyList()

val String.modRoles: List<String>
    get() = settings?.modRoles ?: emptyList()

val String.colourmeRoles: List<String>
    get() = settings?.allowedColourmeRoles ?: emptyList()

val String.owners: List<String>
    get() = settings?.owners ?: listOf("202115709231300617", "256143257472335872", "423915768191647755")

fun String.addModRole(newRoleId: String) {
    if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.push("modRoles", newRoleId)) == null) {
        settingsCollection.insertOne(
            Settings(
                modRoles = listOf(newRoleId),
                guildId = this
            )
        )
    }
}

fun String.removeModRole(roleId: String) {
    settingsCollection.updateOne(Settings::guildId eq this, Updates.pull("modRoles", roleId))
}

fun String.addQuoteRole(newRoleId: String) {
    if (settingsCollection.findOneAndUpdate(
            Settings::guildId eq this,
            Updates.push("allowedQuoteRoles", newRoleId)
        ) == null
    ) {
        settingsCollection.insertOne(
            Settings(
                allowedQuoteRoles = listOf(newRoleId),
                guildId = this
            )
        )
    }
}

fun String.removeQuoteRole(roleId: String) {
    settingsCollection.updateOne(Settings::guildId eq this, Updates.pull("allowedQuoteRoles", roleId))
}

fun String.addColourmeRole(newRoleId: String) {
    if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.push("allowedColourmeRoles", newRoleId)) == null) {
        settingsCollection.insertOne(
            Settings(
                allowedColourmeRoles = listOf(newRoleId),
                guildId = this
            )
        )
    }
}

fun String.removeColourmeRole(roleId: String) {
    settingsCollection.updateOne(Settings::guildId eq this, Updates.pull("allowedColourmeRoles", roleId))
}

fun String.addOwner(ownerId: String) {
    if (settingsCollection.findOneAndUpdate(Settings::guildId eq this, Updates.push("owners", ownerId)) == null) {
        settingsCollection.insertOne(
            Settings(
                allowedQuoteRoles = listOf(ownerId),
                guildId = this
            )
        )
    }
}

fun String.removeOwner(ownerId: String) {
    settingsCollection.updateOne(Settings::guildId eq this, Updates.pull("owners", ownerId))
}