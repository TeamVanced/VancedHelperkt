import di.*
import org.koin.core.context.startKoin

suspend fun main() {
    startKoin {
        modules(
            commandManagerModule,
            mapperModule,
            messageListenerModule,
            repositoryModule,
            serviceModule,
        )
    }

    Bot().start()
}