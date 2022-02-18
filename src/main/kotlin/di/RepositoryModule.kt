package di

import network.service.CoinlibService
import network.service.GenderapiService
import network.service.GuideService
import org.koin.dsl.module
import repository.coin.CoinlibRepository
import repository.coin.CoinlibRepositoryImpl
import repository.genderapi.GenderapiRepository
import repository.genderapi.GenderapiRepositoryImpl
import repository.guide.GuideRepository
import repository.guide.GuideRepositoryImpl

val repositoryModule = module {
    fun provideGuideRepository(
        service: GuideService,
    ): GuideRepository {
        return GuideRepositoryImpl(
            service = service,
        )
    }

    fun provideGenderRepository(
        service: GenderapiService,
    ): GenderapiRepository {
        return GenderapiRepositoryImpl(
            service = service,
        )
    }

    fun provideCoinRepository(
        service: CoinlibService,
    ): CoinlibRepository {
        return CoinlibRepositoryImpl(
            service = service,
        )
    }

    single { provideGuideRepository(get()) }
    single { provideGenderRepository(get()) }
    single { provideCoinRepository(get()) }
}