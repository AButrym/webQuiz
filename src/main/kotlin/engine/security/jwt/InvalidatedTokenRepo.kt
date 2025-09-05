package engine.security.jwt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface InvalidatedTokenRepo : JpaRepository<InvalidatedTokenEntity, String> {
    fun existsByTokenId(tokenId: String): Boolean

    @Modifying
    @Query("delete from InvalidatedToken t where t.expirationTime < :expirationTime")
    fun deleteByExpirationTimeBefore(expirationTime: Long)
}