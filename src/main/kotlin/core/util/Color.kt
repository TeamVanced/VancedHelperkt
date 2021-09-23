package core.util

import dev.kord.common.Color

val randomColor get() = Color((Math.random() * 0x1000000).toInt())