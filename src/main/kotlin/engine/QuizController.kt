package engine

import engine.model.CreateQuizRequest
import engine.model.QuizAnswerFeedback
import engine.model.QuizItem
import engine.model.SolveReq
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quizzes")
class QuizController(
    private val quizService: QuizService
) {
    private val log = LoggerFactory.getLogger(QuizController::class.java)

    @PostMapping
    fun create(@RequestBody @Valid
               quiz: CreateQuizRequest): QuizItem {
        log.info("Creating quiz: $quiz")
        return quizService.createQuiz(quiz)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Int): QuizItem =
        quizService.getQuiz(id)

    @GetMapping
    fun getAll(): Collection<QuizItem> =
        quizService.getAll()

    @PostMapping("/{id}/solve")
    fun solve(@PathVariable id: Int,
              @RequestBody solveReq: SolveReq): QuizAnswerFeedback =
        quizService.evalAnswer(id, solveReq.answer.toSet())
}