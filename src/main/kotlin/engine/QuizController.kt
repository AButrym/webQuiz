package engine

import engine.common.logger
import engine.model.*
import engine.security.IsCreator
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/quizzes")
class QuizController(
    private val quizService: QuizService
) {
    private val log = logger()

    @IsCreator
    @GetMapping("/test")
    fun foo() {}

//    @PreAuthorize("hasRole('CREATOR')")
    @IsCreator
    @PostMapping
    fun create(
        @RequestBody @Valid
        quiz: CreateQuizRequest
    ): ResponseEntity<QuizItem> {
        val quizItem = quizService.createQuiz(quiz).also {
            log.debug("Creating quiz: {}", it)
        }
        val uri: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(quizItem.id)
            .toUri()
        return ResponseEntity.created(uri).body(quizItem)
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') and @auth.canDeleteQuizId(#id)")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<*> {
        quizService.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}