package repository.guide

import domain.model.guide.GuideJsonModel

interface GuideRepository {

    suspend fun fetch(jsonName: String): GuideJsonModel

}