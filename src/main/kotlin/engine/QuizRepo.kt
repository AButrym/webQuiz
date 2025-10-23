package engine

import engine.model.entity.QuizEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuizRepo: JpaRepository<QuizEntity, Int>