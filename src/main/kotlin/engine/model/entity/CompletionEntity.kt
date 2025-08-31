package engine.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime


@Entity(name = "Completion")
@Table(name = "completions")
class CompletionEntity(
    @Id @GeneratedValue
    override var id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_item_id")
    var quizItem: QuizItemEntity? = null,

    @CreatedDate
    var completedAt: LocalDateTime? = null

) : JpaBase()