package core.collection

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import database.guildDBObject
import org.bson.conversions.Bson

fun <E, TDocument> mongoMutableListOf(
    parameter: MongoListParameter<E, TDocument>
) = MongoMutableList(parameter)

data class MongoListParameter<E, TDocument>(
    val name: String,
    val list: List<E>,
    val collection: MongoCollection<TDocument>,
)

class MongoMutableList<E, TDocument>(
    private val parameter: MongoListParameter<E, TDocument>,
) : ArrayList<E>(parameter.list) {

    override fun add(element: E): Boolean {
        mutateCollection(Updates.push(parameter.name, element))
        return super.add(element)
    }

    override fun remove(element: E): Boolean {
        mutateCollection(Updates.pull(parameter.name, element))
        return super.remove(element)
    }

    private fun mutateCollection(
        action: Bson
    ) {
        parameter.collection.findOneAndUpdate(
            guildDBObject,
            action
        )
    }

}