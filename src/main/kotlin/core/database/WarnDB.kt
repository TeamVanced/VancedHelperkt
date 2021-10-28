package core.database

import com.mongodb.client.model.Updates
import config

import core.database.collections.Warn
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val warnsCollection = helperDB.getCollection<Warn>("warns")

fun getUserWarns(
    userId: String
) = warnsCollection.findOne(
    guildDBObject.append("userId", userId)
)

fun deleteUserWarns(
    userId: String
) {
    warnsCollection.findOneAndDelete(
        guildDBObject.append("userId", userId)
    )
}

fun addUserWarn(
    userId: String,
    userTag: String,
    reason: String,
) {
    if (
        warnsCollection.findOneAndUpdate(
            guildDBObject.append("userId", userId),
            Updates.push("reasons", reason)
        ) == null
    ) {
        warnsCollection.insertOne(
            Warn(
                guildId = config.guildId,
                userId = userId,
                userName = userTag,
                reasons = listOf(reason)
            )
        )
    }
}

fun removeUserWarn(
    userId: String,
    warnId: Int?
) {
    if (warnId == null) {
        warnsCollection.findOneAndUpdate(
            guildDBObject.append("userId", userId),
            Updates.popLast("reasons")
        )
        return
    }

    warnsCollection.findOneAndUpdate(
        guildDBObject.append("userId", userId),
        Updates.unset("reasons.${warnId - 1}")
    )
}