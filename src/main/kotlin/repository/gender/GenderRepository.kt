package repository.gender

import domain.model.gender.GenderModel

interface GenderRepository {

    suspend fun get(
        token: String,
        name: String
    ): GenderModel

}