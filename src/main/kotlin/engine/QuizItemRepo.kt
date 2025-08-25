package engine

import engine.model.entity.QuizItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuizItemRepo : JpaRepository<QuizItemEntity, Int> {
}