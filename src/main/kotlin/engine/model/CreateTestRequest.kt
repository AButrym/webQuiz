package engine.model

import jakarta.validation.constraints.NotBlank

data class CreateTestRequest(
    @field:NotBlank(message = "Title must not be blank")
    val title: String,
    val quizItems: List<Int>?
)
