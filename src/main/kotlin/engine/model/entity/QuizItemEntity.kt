package engine.model.entity

import engine.model.QuizItem
import jakarta.persistence.*

@Entity
@Table(name = "quiz_items")
data class QuizItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

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
) {
    fun toQuizItem() = QuizItem(
        id ?: error("Quiz item ID not set"),
        title,
        text,
        options
    )
}
//
//@Entity(name = "QuizItem")
//@Table(name = "quiz_items")
//class QuizItemEntity(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    override var id: Int? = null,
//    var title: String = "",
//    var text: String = "",
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "quiz_options", joinColumns = [JoinColumn(name = "quiz_item_id")])
//    @Column(name = "option")
//    var options: MutableList<String> = mutableListOf(),
//
//    @OneToOne(mappedBy = "quizItem", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
//    var answer: AnswerEntity? = null
//) : JpaBase() {
//    fun toQuizItem() = QuizItem(
//        id ?: error("Quiz item ID not set"),
//        title,
//        text,
//        options
//    )
//}
