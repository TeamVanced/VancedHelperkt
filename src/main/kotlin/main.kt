import di.*
import org.koin.core.context.startKoin

suspend fun main() {
    startKoin {
        modules(
            commandManagerModule,
            mapperModule,
            eventListenerModule,
            repositoryModule,
            serviceModule,
        )
    }

    Bot().start()
}