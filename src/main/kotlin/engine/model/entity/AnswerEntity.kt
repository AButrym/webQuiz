package engine.model.entity


import jakarta.persistence.*

@Entity(name = "Answer")
@Table(name = "answers")
class AnswerEntity(
    @Id
    override var id: Int? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    var quizItem: QuizItemEntity? = null,

    @ElementCollection
    @CollectionTable(name = "answer_options", joinColumns = [JoinColumn(name = "answer_id")])
    @Column(name = "option")
    var correctOptions: MutableList<Int> = mutableListOf()
) : JpaBase()