package di

import com.google.gson.GsonBuilder
import network.service.CoinlibService
import network.service.GenderapiService
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

    fun provideGenderService(): GenderapiService {
        return Retrofit.Builder()
            .baseUrl(genderApiUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GenderapiService::class.java)
    }

    fun provideCoinService(): CoinlibService {
        return Retrofit.Builder()
            .baseUrl(coinlibApiUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CoinlibService::class.java)
    }

    single { provideGuideService() }
    single { provideGenderService() }
    single { provideCoinService() }
}