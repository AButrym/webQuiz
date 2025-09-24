package engine.security

import engine.QuizItemRepo
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component("auth")
class SecurityUtils(
    private val quizItemRepo: QuizItemRepo
) {
    fun getLoggedUserId(): Int =
        (SecurityContextHolder.getContext()
            .authentication.principal
                as UserDetailsImpl).id

    fun canDeleteQuizId(quizId: Int): Boolean {
        val quiz = quizItemRepo.findById(quizId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id $quizId not found") }
        return quiz.ownerId == getLoggedUserId()
    }
}