package engine

import engine.model.entity.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepo : JpaRepository<AnswerEntity, Int> {
}