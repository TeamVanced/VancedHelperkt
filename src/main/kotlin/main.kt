import di.*
import org.koin.core.context.startKoin

suspend fun main() {
    startKoin {
        modules(
            commandManagerModule,
            eventListenerModule,
            ktorModule,
            repositoryModule,
            serviceModule,
        )
    }

    Bot().start()
}