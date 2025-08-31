package engine

import engine.model.CompletionDto
import engine.model.entity.CompletionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CompletionRepo : JpaRepository<CompletionEntity, Int> {
    @Query("select new engine.model.CompletionDto(c.quizItem.id, c.completedAt) from Completion c where c.user.id = :userId order by c.completedAt DESC")
    fun findByUserId(@Param("userId") userId: Int, pageable: Pageable): Page<CompletionDto>
}