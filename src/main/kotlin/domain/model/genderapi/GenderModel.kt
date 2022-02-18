package domain.model.genderapi

import network.model.genderapi.GenderDto

data class GenderModel(
    val gender: String,
    val accuracy: Int
) {
    companion object {
        fun fromDto(dto: GenderDto): GenderModel {
            return GenderModel(
                gender = dto.gender,
                accuracy = dto.accuracy
            )
        }
    }
}
