package ext

import commands.ArgumentType

fun String.optional(): Pair<String, ArgumentType> = this to ArgumentType.Optional

fun String.required(): Pair<String, ArgumentType> = this to ArgumentType.Required

fun Map.Entry<String, ArgumentType>.transformToArg(): String = "${value.prefix}$key${value.suffix}"