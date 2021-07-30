package database

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Updates
import config
import database.collections.Emote
import database.collections.EmoteRole
import database.collections.Settings
import database.collections.Warn
import org.litote.kmongo.KMongo
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val client: MongoClient = KMongo.createClient(config.mongoString)
val helperDB: MongoDatabase = client.getDatabase("VancedHelper")

val settingsCollection = helperDB.getCollection<Settings>()
val warnsCollection = helperDB.getCollection<Warn>("warns")
val emotesCollection = helperDB.getCollection<Emote>("emotes")
val emoteRolesCollection = helperDB.getCollection<EmoteRole>("emoteRoles")

fun getEmoteRoles(
    messageId: String,
    emote: String
): EmoteRole? = emoteRolesCollection.findOne(
    BasicDBObject("guildId", config.guildId)
        .append("messageId", messageId)
        .append("emote", emote)
)
fun updateEmoteRoles(
    messageId: String,
    emote: String,
    roleId: String
): EmoteRole? = emoteRolesCollection.findOneAndUpdate(
    BasicDBObject("guildId", config.guildId)
        .append("messageId", messageId)
        .append("emote", emote),
    Updates.set("roleId", roleId)
)