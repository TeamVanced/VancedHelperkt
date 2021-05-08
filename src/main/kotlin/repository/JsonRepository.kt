package repository

import domain.model.GuideJsonModel

interface JsonRepository {

    suspend fun fetch(jsonName: String): GuideJsonModel

}