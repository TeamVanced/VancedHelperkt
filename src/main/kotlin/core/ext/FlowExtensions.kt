package core.ext

import dev.kord.core.entity.Entity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

suspend inline fun <T : Entity, R : Comparable<R>> Flow<T>.sortedByDescending(
    crossinline selector: (T) -> R?
) = flow {
    for (entity in toList().sortedByDescending(selector)) {
        emit(entity)
    }
}