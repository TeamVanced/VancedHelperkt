package core.ext

fun String.takeMax(n: Int): String {
    return if (this.length > n) {
        this.take(n - 3) + "..."
    } else this
}