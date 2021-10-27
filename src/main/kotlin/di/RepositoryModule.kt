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
import repository.coin.CoinRepository
import repository.coin.CoinRepositoryImpl
import repository.country.CountryRepository
import repository.country.CountryRepositoryImpl
import repository.gender.GenderRepository
import repository.gender.GenderRepositoryImpl
import repository.guide.GuideRepository
import repository.guide.GuideRepositoryImpl

val repositoryModule = module {

    fun provideGuideRepository(
        service: GuideService,
        mapper: GuideJsonDtoMapper
    ): GuideRepository = GuideRepositoryImpl(
        service = service,
        mapper = mapper
    )

    fun provideGenderRepository(
        service: GenderService,
        mapper: GenderDtoMapper
    ): GenderRepository = GenderRepositoryImpl(
        service = service,
        mapper = mapper
    )

    fun provideCountryRepository(
        service: CountryService,
        mapper: CountryDtoMapper
    ): CountryRepository = CountryRepositoryImpl(
        service = service,
        mapper = mapper
    )

    fun provideCoinRepository(
        service: CoinService,
        mapper: CoinDtoMapper
    ): CoinRepository = CoinRepositoryImpl(
        service = service,
        mapper = mapper
    )

    single { provideGuideRepository(get(), get()) }
    single { provideGenderRepository(get(), get()) }
    single { provideCountryRepository(get(), get()) }
    single { provideCoinRepository(get(), get()) }
}