package engine.security

import engine.model.entity.DenylistEntity
import engine.security.jwt.DenylistRepo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

@Component
class JwtService(
    @param:Value("\${jwt.secret:default-secret-which-is-long-enough-32-characters-xyzxyz}")
    private val jwtSecret: String,
    @param:Value("\${jwt.access-ttl-seconds:900}")
    private val accessTtlSeconds: Long,
    @param:Value("\${jwt.refresh-ttl-seconds:2592000}")
    private val refreshTtlSeconds: Long,
    private val denylistRepo: DenylistRepo
) {
    private val key by lazy { Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8)) }

    fun generateAccessToken(userId: Int, tokenId: String): String {
        val now = Instant.now()
        val exp = now.plusSeconds(accessTtlSeconds)
        return Jwts.builder()
            .subject(userId.toString())
            .id(tokenId)
            .expiration(Date.from(exp))
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(userId: Int): String {
        val now = Instant.now()
        val exp = now.plusSeconds(refreshTtlSeconds)
        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(userId.toString())
            .expiration(Date.from(exp))
            .audience().add("refresh").and()
            .signWith(key)
            .compact()
    }

    fun parseSubject(token: String): String? = runCatching {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
    }.getOrNull()

    fun parseId(token: String): String? = runCatching {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.id
    }.getOrNull()

    fun validate(token: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload.let { claims ->
                claims.expiration.after(Date()) &&
                !denylistRepo.existsByTokenId(claims.id)
            }
    }.getOrDefault(false)

    fun validateRefresh(refreshToken: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(refreshToken)
            .payload.let { claims ->
                claims.audience.contains("refresh") &&
                        claims.expiration.after(Date()) &&
                        !denylistRepo.existsByTokenId(claims.id)
            }
    }.getOrDefault(false)

    fun invalidate(refreshToken: String) {
        val payload = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(refreshToken).payload
        DenylistEntity(
            tokenId = payload.id,
            expirationTime = payload.expiration.time
        ).let { denylistRepo.save(it) }
    }
}