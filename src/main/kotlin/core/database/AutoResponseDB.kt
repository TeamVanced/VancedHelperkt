package core.database

import core.database.collections.Keyword
import core.database.collections.Response
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val responseCollection = helperDB.getCollection<Response>("autoresponses-responses")
val keywordCollection = helperDB.getCollection<Keyword>("autoresponses-keywords")

fun addResponse(message: String): Int {
    val index = (responseCollection.find().lastOrNull()?.index ?: 0) + 1
    responseCollection.insertOne(
        Response(
            message = message,
            index = index
        )
    )
    return index
}

fun addKeyword(keyword: String, indexOfResponse: Int) {
    keywordCollection.insertOne(
        Keyword(
            keyword = keyword,
            indexOfResponse = indexOfResponse
        )
    )
}

fun getResponses(): List<Response> {
    return responseCollection.find().toList()
}

fun getResponseForKeyword(keyword: String): Response? {
    val keywordDoc = keywordCollection.findOne(Keyword::keyword eq keyword)
        ?: return null
    return responseCollection.find(
        Response::index eq keywordDoc.indexOfResponse
    ).firstOrNull()
}

fun getKeywords(): List<Keyword> {
    return keywordCollection.find().toList()
}

fun removeResponse(index: Int) {
    responseCollection.findOneAndDelete(
        Response::index eq index
    )
}

fun removeKeyword(keyword: String) {
    keywordCollection.findOneAndDelete(
        Keyword::keyword eq keyword
    )
}