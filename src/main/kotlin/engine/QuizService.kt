package engine

import engine.model.CompletionDto
import engine.model.CreateQuizRequest
import engine.model.QuizAnswerFeedback
import engine.model.QuizItem
import engine.model.entity.CompletionEntity
import engine.model.entity.QuizItemEntity
import engine.security.SecurityUtils
import engine.users.UserRepo
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

@Transactional(readOnly = true)
@Service
class QuizService(
    private val quizItemRepo: QuizItemRepo,
    private val completionRepo: CompletionRepo,
    private val userRepo: UserRepo,
    private val secUtils: SecurityUtils
) {
    private val log = LoggerFactory.getLogger(QuizService::class.java)

    private companion object {
        private const val QUIZZES_PAGE_SIZE = 10
        fun Pageable.withDefaultPageSize() =
            if (pageSize == QUIZZES_PAGE_SIZE) {
                this
            } else {
                PageRequest.of(pageNumber, QUIZZES_PAGE_SIZE, sort)
            }
    }

    @Transactional
    fun createQuiz(quiz: CreateQuizRequest): QuizItem =
        QuizItemEntity().apply {
            ownerId = secUtils.getLoggedUserId()
            title = quiz.title
            text = quiz.text
            options = quiz.options.orEmpty().toMutableList()
            correctOptions = quiz.answer.orEmpty().toMutableList()
        }.let {
            quizItemRepo.save(it)
        }.toQuizItem()

    fun getQuiz(id: Int): QuizItem =
        quizItemRepo.findById(id).getOrNull()
            ?.toQuizItem()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $id not found")

    fun getAll(pageable: Pageable): Page<QuizItem> {
        return quizItemRepo.findAll(pageable.withDefaultPageSize())
            .map { it.toQuizItem() }
    }

    fun getAllCompletions(pageable: Pageable): Page<CompletionDto> {
        val userId = secUtils.getLoggedUserId()
        return completionRepo.findByUserId(userId, pageable.withDefaultPageSize())
    }

    @Transactional
    fun evalAnswer(quizId: Int, answer: Set<Int>): QuizAnswerFeedback {
        val correct = quizItemRepo.findCorrectOptionIndexes(quizId)

        if (correct.isEmpty() && !quizItemRepo.existsById(quizId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $quizId not found")
        }

        return if (answer == correct) {
            completionRepo.saveAndFlush(
                CompletionEntity().apply {
                    quizItem = quizItemRepo.getReferenceById(quizId)
                    user = userRepo.getReferenceById(secUtils.getLoggedUserId())
                }
            ).let { log.trace("Saved completion: {}", it) }
            QuizAnswerFeedback.OK
        } else
            QuizAnswerFeedback.FAIL
    }

    @Transactional
    fun delete(quizId: Int) {
        val quiz = quizItemRepo.findById(quizId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $quizId not found") }
        if (quiz.ownerId != secUtils.getLoggedUserId()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this quiz")
        }
        quizItemRepo.delete(quiz)
    }
}
