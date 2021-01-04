package utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.net.URL

fun String.getJson(): JsonObject? {
    return Parser.default().parse(
        StringBuilder(URL(this).readText().trimIndent())
    ) as JsonObject?
}