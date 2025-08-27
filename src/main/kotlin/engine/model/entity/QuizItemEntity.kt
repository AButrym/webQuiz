package engine.model.entity

import engine.model.QuizItem
import jakarta.persistence.*

@Entity
@Table(name = "quiz_items")
class QuizItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,

    var ownerId: Int? = null,

    var title: String = "",
    var text: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "quiz_options",
        joinColumns = [JoinColumn(name = "quiz_id")]
    )
    @Column(name = "option_text")
    @OrderColumn(name = "option_ix")
    var options: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "answers",
        joinColumns = [JoinColumn(name = "quiz_id")]
    )
    @Column(name = "option_ix")
    var correctOptions: MutableList<Int> = mutableListOf()
) : JpaBase() {
    fun toQuizItem() = QuizItem(
        id ?: error("Quiz item ID not set"),
        title,
        text,
        options
    )
}
