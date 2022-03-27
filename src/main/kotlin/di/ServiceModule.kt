package di

import com.google.gson.GsonBuilder
import network.service.CoinlibService
import network.service.GenderapiService
import network.util.coinlibApiUrl
import network.util.genderApiUrl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serviceModule = module {
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
    
    single { provideGenderService() }
    single { provideCoinService() }
}