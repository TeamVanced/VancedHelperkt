package di

import core.listener.MessageListener
import core.listener.ReactionListener
import org.koin.dsl.module

val eventListenerModule = module {
    single { MessageListener() }
    single { ReactionListener() }
}