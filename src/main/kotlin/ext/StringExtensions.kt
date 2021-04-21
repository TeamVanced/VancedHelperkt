package ext

import commands.ArgumentType

fun String.optional(): Pair<String, ArgumentType> = this to ArgumentType.Optional

fun String.required(): Pair<String, ArgumentType> = this to ArgumentType.Required

fun String.takeMax(amount: Int): String {
    return if (this.length > amount) {
        this.take(amount - 3) + "..."
    } else this
}

fun Map.Entry<String, ArgumentType>.transformToArg(): String = "${value.prefix}$key${value.suffix}"