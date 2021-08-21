package di

import core.message.MessageListener
import org.koin.dsl.module

val messageListenerModule = module {
    single { MessageListener() }
}