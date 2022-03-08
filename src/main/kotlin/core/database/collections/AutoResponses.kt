package core.database.collections

data class Response(
    val message: String,
    val index: Int,
)

data class Keyword(
    val keyword: String,
    val indexOfResponse: Int
)