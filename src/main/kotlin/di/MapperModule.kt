package di

import network.model.coin.CoinDtoMapper
import network.model.country.CountryDtoMapper
import network.model.gender.GenderDtoMapper
import network.model.guide.GuideJsonDtoMapper
import org.koin.dsl.module

val mapperModule = module {

    fun provideJsonDtoMapper(): GuideJsonDtoMapper {
        return GuideJsonDtoMapper()
    }

    fun provideGenderMapper(): GenderDtoMapper {
        return GenderDtoMapper()
    }

    fun provideCountryMapper(): CountryDtoMapper {
        return CountryDtoMapper()
    }

    fun provideCoinMapper(): CoinDtoMapper {
        return CoinDtoMapper()
    }

    single { provideJsonDtoMapper() }
    single { provideGenderMapper() }
    single { provideCountryMapper() }
    single { provideCoinMapper() }
}