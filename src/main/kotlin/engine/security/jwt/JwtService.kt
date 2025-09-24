package engine.security.jwt

import engine.security.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import java.util.UUID

@Component
class JwtService(
    @param:Value("\${jwt.secret:default-secret-which-is-long-enough-32-characters-xyzxyz}")
    private val jwtSecret: String,

    @param:Value("\${jwt.access-ttl-seconds:1800}")
    private val accessTtlSeconds: Long,

    @param:Value("\${jwt.refresh-ttl-seconds:2592000}")
    private val refreshTtlSeconds: Long,

    private val invalidatedTokenRepo: InvalidatedTokenRepo
) {
    private val key by lazy { Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8)) }

    fun generateTokens(userId: Int, role: Role): JwtTokensDto {
        val now = Instant.now()
        val expAccess = now.plusSeconds(accessTtlSeconds)
        val expRefresh = now.plusSeconds(refreshTtlSeconds)
        val tokenId = UUID.randomUUID().toString()

        val accessToken = Jwts.builder()
            .subject(userId.toString())
            .id(tokenId)
            .expiration(Date.from(expAccess))
            .claim("role", role.name)
            .signWith(key)
            .compact()
        val refreshToken = Jwts.builder()
            .subject(userId.toString())
            .id(tokenId)
            .expiration(Date.from(expRefresh))
            .claim("role", role.name)
            .audience().add("refresh").and()
            .signWith(key)
            .compact()
        return JwtTokensDto(accessToken, refreshToken)
    }

    fun parseSubject(token: String): Int? = runCatching {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject.toInt()
    }.getOrNull()

    fun parseRole(token: String): Role? = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload["role"]?.let {
                Role.valueOf(it.toString())
        }
    }.getOrNull()

    fun validate(token: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload.let { claims ->
                claims.expiration.after(Date()) &&
                        !invalidatedTokenRepo.existsByTokenId(claims.id)
            }
    }.getOrDefault(false)

    fun validateRefresh(refreshToken: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(refreshToken)
            .payload.let { claims ->
                claims.audience.contains("refresh") &&
                        claims.expiration.after(Date()) &&
                        !invalidatedTokenRepo.existsByTokenId(claims.id)
            }
    }.getOrDefault(false)

    fun invalidate(refreshToken: String) {
        val payload = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(refreshToken).payload
        InvalidatedTokenEntity(
            tokenId = payload.id,
            expirationTime = payload.expiration.time
        ).let { invalidatedTokenRepo.save(it) }
    }
}