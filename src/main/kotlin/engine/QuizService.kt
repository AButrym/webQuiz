package engine

import engine.model.CreateQuizRequest
import engine.model.QuizAnswerFeedback
import engine.model.QuizItem
import engine.model.entity.QuizItemEntity
import engine.security.UserDetailsImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

@Transactional(readOnly = true)
@Service
class QuizService(
    private val quizItemRepo: QuizItemRepo
) {
    private fun getLoggedUserId(): Int =
        (SecurityContextHolder.getContext()
            .authentication.principal
                as UserDetailsImpl).id

    @Transactional
    fun createQuiz(quiz: CreateQuizRequest): QuizItem =
        QuizItemEntity().apply {
            ownerId = getLoggedUserId()
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

    @Transactional
    fun delete(quizId: Int) {
        val quiz = quizItemRepo.findById(quizId)
            .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $quizId not found") }
        if (quiz.ownerId != getLoggedUserId()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this quiz")
        }
        quizItemRepo.delete(quiz)
    }
}
