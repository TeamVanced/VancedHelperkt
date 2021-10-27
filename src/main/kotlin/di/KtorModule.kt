package di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.dsl.module

val ktorModule = module {
    fun provideKtor() = HttpClient(CIO)

    single { provideKtor() }
}