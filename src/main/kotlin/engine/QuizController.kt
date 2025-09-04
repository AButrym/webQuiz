package engine

import engine.common.logger
import engine.model.*
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quizzes")
class QuizController(
    private val quizService: QuizService
) {
    private val log = logger()

    @PostMapping
    fun create(
        @RequestBody @Valid
        quiz: CreateQuizRequest
    ): QuizItem =
        quizService.createQuiz(quiz).also {
            log.debug("Creating quiz: {}", it)
        }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Int): QuizItem =
        quizService.getQuiz(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<QuizItem> =
        quizService.getAll(pageable)

    @GetMapping("/completed")
    fun getAllCompletions(pageable: Pageable): Page<CompletionDto> =
        quizService.getAllCompletions(pageable)

    @PostMapping("/{id}/solve")
    fun solve(
        @PathVariable id: Int,
        @RequestBody solveReq: SolveReq
    ): QuizAnswerFeedback =
        quizService.evalAnswer(id, solveReq.answer.toSet())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<*> {
        quizService.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}