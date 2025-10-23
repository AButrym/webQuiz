package engine

import engine.model.CreateTestRequest
import engine.model.Quiz
import engine.model.entity.QuizEntity
import engine.security.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

typealias Test = Quiz
typealias TestRepo = QuizRepo
typealias TestEntity = QuizEntity

@Transactional(readOnly = true)
@Service
class TestService(
    private val testRepo: TestRepo,
    private val quizItemRepo: QuizItemRepo,
    private val secUtils: SecurityUtils
) {
    private val log = LoggerFactory.getLogger(TestService::class.java)

    @Transactional
    fun getTest(id: Int) : Test {
        testRepo.findById(id).orElseThrow().let {
            log.debug("Fetched test: {}", it)
            return it.toQuiz()
        }
    }

    @Transactional
    fun getAllTests(): List<Test> {
        return testRepo.findAll().map {
            log.debug("Fetched tests: {}", it)
            it.toQuiz()
        }
    }

    @Transactional
    fun createTest(test: CreateTestRequest): Test =
        TestEntity().apply {
            title = test.title
            test.quizItems?.forEach { itemId ->
                val itemEntity = quizItemRepo.findById(itemId)
                    .orElseThrow { IllegalArgumentException("QuizItem with id $itemId not found") }

                addQuizItem(itemEntity)
            }
        }.let {
            testRepo.save(it)
        }.toQuiz()
}