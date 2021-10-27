package core.database.data

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import core.database.guildDBObject
import org.bson.conversions.Bson
import kotlin.reflect.KProperty

fun <E, TDocument> mongoItem(
    parameter: MongoItemParameter<E, TDocument>
) = MongoItem(parameter)

data class MongoItemParameter<E, TDocument>(
    val name: String,
    val item: E,
    val collection: MongoCollection<TDocument>
)

class MongoItem<E, TDocument>(
    private val parameter: MongoItemParameter<E, TDocument>
) {

    private var value = parameter.item

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ) = value

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        newValue: E
    ) {
        mutateCollection(Updates.set(parameter.name, newValue))
        value = newValue
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