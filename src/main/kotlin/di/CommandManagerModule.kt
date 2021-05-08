package di

import commandhandler.CommandManager
import org.koin.dsl.module

val commandManagerModule = module {
    single { CommandManager() }
}

