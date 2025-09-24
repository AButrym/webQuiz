package engine

import engine.model.QuizItem
import engine.model.entity.QuizItemEntity
import engine.security.SecurityUtils
import engine.users.UserRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class QuizServiceTest {
    @InjectMocks
    private lateinit var quizService: QuizService

    @Mock
    private lateinit var quizItemRepo: QuizItemRepo
    @Mock
    private lateinit var completionRepo: CompletionRepo
    @Mock
    private lateinit var userRepo: UserRepo
    @Mock
    private lateinit var secUtils: SecurityUtils

    @Test
    @DisplayName("getQuiz should return quiz with given id")
    fun test01() {
        val id = 12
        val quizEntity = QuizItemEntity(id)

        doThrow()

        `when`(quizItemRepo.findById(id)).thenReturn(
            Optional.of(quizEntity))

        val res: QuizItem = quizService.getQuiz(id)

        assertThat(res.id)
            .`as`("Quiz id should be equal to given id")
            .isEqualTo(id)

        verify(quizItemRepo, times(1)).findById(id)
    }
}