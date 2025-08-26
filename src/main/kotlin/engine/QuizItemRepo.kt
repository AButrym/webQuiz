package engine

import engine.model.entity.QuizItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QuizItemRepo : JpaRepository<QuizItemEntity, Int> {
    @Query("select a from QuizItemEntity q join q.correctOptions a where q.id = :quizId")
    fun findCorrectOptionIndexes(@Param("quizId") quizId: Int): Set<Int>
}