package di

import com.google.gson.GsonBuilder
import network.service.CoinService
import network.service.CountryService
import network.service.GenderService
import network.service.GuideService
import network.util.baseGuideUrl
import network.util.coinlibApiUrl
import network.util.genderApiUrl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serviceModule = module {

    fun provideGuideService(): GuideService {
        return Retrofit.Builder()
            .baseUrl(baseGuideUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GuideService::class.java)
    }

    fun provideGenderService(): GenderService {
        return Retrofit.Builder()
            .baseUrl(genderApiUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GenderService::class.java)
    }

    fun provideCountryService(): CountryService {
        return Retrofit.Builder()
            .baseUrl(genderApiUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CountryService::class.java)
    }

    fun provideCoinService(): CoinService {
        return Retrofit.Builder()
            .baseUrl(coinlibApiUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CoinService::class.java)
    }

    single { provideGuideService() }
    single { provideGenderService() }
    single { provideCountryService() }
    single { provideCoinService() }

}