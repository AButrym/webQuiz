package engine.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CompletionDto(
    val id: Int,
    val completedAt: String
) {
    constructor(id: Int?, completedAt: LocalDateTime?) : this(
        id ?: error("Quiz ID for a completion cannot be null"),
        completedAt?.format(formatter) ?: "null"
    )

    companion object {
//        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}
