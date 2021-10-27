package di

import core.listener.MessageListener
import core.listener.UserListener
import org.koin.dsl.module

val eventListenerModule = module {
    single { MessageListener() }
    single { UserListener() }
}