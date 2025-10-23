package engine.config

import engine.QuizItemRepo
import engine.QuizRepo
import engine.model.entity.QuizEntity
import engine.model.entity.QuizItemEntity
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Initial DB data population
 *
 * @param quizItemRepo Repository for quiz items
 * @param quizRepo Repository for quizzes
 */
@Component
class DataLoader(
    private val quizItemRepo: QuizItemRepo,
    private val quizRepo: QuizRepo
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val quiz = quizRepo.save<QuizEntity>(QuizEntity(
            title = "Sample Quiz",
        ))

        val item1 = QuizItemEntity(
            title = "What is the capital of France?",
            text = "Choose the correct option.",
            options = mutableListOf("Berlin", "Madrid", "Paris", "Rome"),
            correctOptions = mutableListOf(2)
        )

        val item2 = QuizItemEntity(
            title = "What is the largest planet in our Solar System?",
            text = "Choose the correct option.",
            options = mutableListOf("Earth", "Jupiter", "Mars", "Saturn"),
            correctOptions = mutableListOf(1)
        )

        val item3 = QuizItemEntity(
            title = "What is the chemical symbol for water?",
            text = "Choose the correct option.",
            options = mutableListOf("H2O", "CO2", "O2", "NaCl"),
            correctOptions = mutableListOf(0)
        )

        val item4 = QuizItemEntity(
            title = "What is 2 + 2?",
            text = "Choose the correct option.",
            options = mutableListOf("3", "4", "5", "6"),
            correctOptions = mutableListOf(1)
        )

        item1.quiz = quiz
        quizItemRepo.save(item1)

        item2.quiz = quiz
        quizItemRepo.save(item2)

        item3.quiz = quiz
        quizItemRepo.save(item3)

        item4.quiz = quiz
        quizItemRepo.save(item4)
    }
}
