package engine.model

data class QuizItem(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>
)
