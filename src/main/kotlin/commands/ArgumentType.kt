package commands

enum class ArgumentType(val prefix: String, val suffix: String) {

    Required("<", ">"), Optional("[", "]")

}