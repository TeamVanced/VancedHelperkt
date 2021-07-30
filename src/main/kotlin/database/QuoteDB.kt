package database

import com.mongodb.client.model.Updates
import database.collections.Quote
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val quotesCollection = helperDB.getCollection<Quote>("quotes")

val allQuotes get() = quotesCollection.find().toList()
val randomQuote: Quote? get() = allQuotes.random()
val lastQuote get() = allQuotes.lastOrNull()

fun getQuote(
    quoteId: Int
): Quote? = quotesCollection.findOne(
    guildDBObject.append("quoteId", quoteId)
)

fun quoteExists(
    messageId: String
) = quotesCollection.findOne(
    guildDBObject.append("messageID", messageId)
) != null

fun searchQuotes(
    keyword: String
): List<Quote> = quotesCollection.find(
    guildDBObject.append("messageContent", keyword)
).toList()

fun addQuote(
    quote: Quote
) {
    quotesCollection.insertOne(quote)
}

fun deleteQuote(
    quoteId: Int
) {
    quotesCollection.deleteOne(
        guildDBObject.append("quoteId", quoteId)
    )
}

fun starQuote(
    quoteId: Int,
    authorId: String,
) {
    quotesCollection.updateOne(
        guildDBObject.append("quoteId", quoteId),
        Updates.push("stars", authorId)
    )
}

fun unstarQuote(
    quoteId: Int,
    authorId: String,
) {
    quotesCollection.updateOne(
        guildDBObject.append("quoteId", quoteId),
        Updates.pull("stars", authorId)
    )
}