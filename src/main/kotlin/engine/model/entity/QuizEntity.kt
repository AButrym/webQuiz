package engine.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "quizzes")
class QuizEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,

    var title: String = "",
    @OneToMany(
        mappedBy = "quiz",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY)
    var quizItems: MutableList<QuizItemEntity> = mutableListOf()
) : JpaBase() {
    fun toQuiz() = engine.model.Quiz(
        id ?: error("Quiz ID not set"),
        title, // Title is not stored in this entity
        quizItems.map { it.id ?: error("Quiz item ID not set") }// Map quiz item IDs to QuizItem objects as needed
    )

    fun addQuizItem(item: QuizItemEntity) {
        item.quiz = this
        quizItems.add(item)
    }

    fun removeQuizItem(item: QuizItemEntity) {
        if (quizItems.remove(item)) {
            item.quiz = null
        }
    }
}