package di

import network.model.coin.CoinDtoMapper
import network.model.country.CountryDtoMapper
import network.model.gender.GenderDtoMapper
import network.model.guide.GuideJsonDtoMapper
import network.service.CoinService
import network.service.CountryService
import network.service.GenderService
import network.service.GuideService
import org.koin.dsl.module
import repository.coin.CoinRepositoryImpl
import repository.country.CountryRepositoryImpl
import repository.gender.GenderRepositoryImpl
import repository.guide.GuideRepositoryImpl

val repositoryModule = module {

    fun provideGuideRepository(
        service: GuideService,
        mapper: GuideJsonDtoMapper
    ): GuideRepositoryImpl {
        return GuideRepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    fun provideGenderRepository(
        service: GenderService,
        mapper: GenderDtoMapper
    ): GenderRepositoryImpl {
        return GenderRepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    fun provideCountryRepository(
        service: CountryService,
        mapper: CountryDtoMapper
    ): CountryRepositoryImpl {
        return CountryRepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    fun provideCoinRepository(
        service: CoinService,
        mapper: CoinDtoMapper
    ): CoinRepositoryImpl {
        return CoinRepositoryImpl(
            service = service,
            mapper = mapper
        )
    }

    single { provideGuideRepository(get(), get()) }
    single { provideGenderRepository(get(), get()) }
    single { provideCountryRepository(get(), get()) }
    single { provideCoinRepository(get(), get()) }
}