package engine

import engine.model.CreateQuizRequest
import engine.model.QuizAnswerFeedback
import engine.model.QuizItem
import engine.model.entity.QuizItemEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

@Transactional(readOnly = true)
@Service
class QuizService(
    private val quizItemRepo: QuizItemRepo
) {
    @Transactional
    fun createQuiz(quiz: CreateQuizRequest): QuizItem =
        QuizItemEntity().apply {
            title = quiz.title
            text = quiz.text
            options = quiz.options.orEmpty().toMutableList()
            correctOptions =
                quiz.answer.orEmpty().toMutableList()
        }.let {
            quizItemRepo.save(it)
        }.toQuizItem()

    fun getQuiz(id: Int): QuizItem =
        quizItemRepo.findById(id).getOrNull()
            ?.toQuizItem()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $id not found")

    fun getAll() = quizItemRepo.findAll().map { it.toQuizItem() }

    fun evalAnswer(quizId: Int, answer: Set<Int>): QuizAnswerFeedback {
        val correct = quizItemRepo.findCorrectOptionIndexes(quizId)

        if (correct.isEmpty() && !quizItemRepo.existsById(quizId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $quizId not found")
        }

        return if (answer == correct) QuizAnswerFeedback.OK else QuizAnswerFeedback.FAIL
    }
}