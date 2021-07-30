package di

import core.command.CommandManager
import org.koin.dsl.module

val commandManagerModule = module {
    single { CommandManager() }
}

