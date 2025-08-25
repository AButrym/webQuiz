package engine.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateQuizRequest(
    @field:NotBlank(message = "Title must not be blank")
    val title: String,
    @field:NotBlank(message = "Text must not be blank")
    val text: String,
    @field:Size(min = 2, message = "You must provide at least two options")
    @field:NotNull(message = "Options must not be null")
    val options: List<String>?,
    val answer: List<Int>?
)
