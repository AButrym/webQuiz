package engine.model

sealed class QuizAnswerFeedback(
    val success: Boolean,
    val feedback: String
) {
    object OK: QuizAnswerFeedback(true, "Congratulations, you're right!")
    object FAIL:QuizAnswerFeedback(false, "Wrong answer! Please, try again.")
}