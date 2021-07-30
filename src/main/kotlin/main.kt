import di.commandManagerModule
import di.mapperModule
import di.repositoryModule
import di.serviceModule
import org.koin.core.context.startKoin

suspend fun main() {
    startKoin {
        modules(
            commandManagerModule,
            mapperModule,
            repositoryModule,
            serviceModule,
        )
    }

    Bot().start()
}