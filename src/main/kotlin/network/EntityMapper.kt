package network

interface EntityMapper<Entity, Model> {

    suspend fun mapToModel(entity: Entity): Model

}