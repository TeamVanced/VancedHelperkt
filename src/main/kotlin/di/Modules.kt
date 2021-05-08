package di

import com.google.gson.GsonBuilder
import commandhandler.CommandManager
import network.JsonService
import network.model.GuideJsonDtoMapper
import network.util.baseGuideUrl
import network.util.localGuideUrl
import org.koin.dsl.module
import repository.JsonRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val commandManagerModule = module {
    single { CommandManager() }
}

val retrofitModule = module {
    fun provideRetrofitService(): JsonService {
        return Retrofit.Builder()
            .baseUrl(baseGuideUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(JsonService::class.java)
    }

    single { provideRetrofitService() }
}

val mapperModule = module {
    fun provideJsonDtoMapper(): GuideJsonDtoMapper {
        return GuideJsonDtoMapper()
    }

    single { provideJsonDtoMapper() }
}

val repositoryModule = module {
    fun provideRepository(
        service: JsonService,
        mapper: GuideJsonDtoMapper
    ): JsonRepositoryImpl {
        return JsonRepositoryImpl(
            service, mapper
        )
    }

    single { provideRepository(get(), get()) }
}