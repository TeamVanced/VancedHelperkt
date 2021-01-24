package application

enum class ApplicationCommandOptionType(val value: Int) {
    SUB_COMMAND(1,),
    SUB_COMMAND_GROUP(1),
    STRING(3),
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8)
}