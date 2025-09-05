package engine.security.jwt

import engine.model.entity.DenylistEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface DenylistRepo : JpaRepository<DenylistEntity, String> {
    fun existsByTokenId(tokenId: String): Boolean

    @Transactional
    @Modifying
    @Query("delete from DenylistEntity t where t.expirationTime < :expirationTime")
    fun deleteByExpirationTimeBefore(expirationTime: Long)
}