package di

import network.service.CoinlibService
import network.service.GenderapiService
import org.koin.dsl.module
import repository.coin.CoinlibRepository
import repository.coin.CoinlibRepositoryImpl
import repository.genderapi.GenderapiRepository
import repository.genderapi.GenderapiRepositoryImpl

val repositoryModule = module {
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

    single { provideGenderRepository(get()) }
    single { provideCoinRepository(get()) }
}