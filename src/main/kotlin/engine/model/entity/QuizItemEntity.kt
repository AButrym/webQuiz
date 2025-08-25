package engine.model.entity

import engine.model.QuizItem
import jakarta.persistence.*

@Entity(name = "QuizItem")
@Table(name = "quiz_items")
class QuizItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,
    var title: String = "",
    var text: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quiz_options", joinColumns = [JoinColumn(name = "quiz_item_id")])
    @Column(name = "option")
    var options: MutableList<String> = mutableListOf(),

    @OneToOne(mappedBy = "quizItem", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var answer: AnswerEntity? = null
) : JpaBase() {
    fun toQuizItem() = QuizItem(
        id ?: error("Quiz item ID not set"),
        title,
        text,
        options
    )
}
