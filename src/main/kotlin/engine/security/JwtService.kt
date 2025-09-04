package engine.security

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
) {
    private val key by lazy { Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8)) }

    fun generateAccessToken(userId: Int): String {
        val now = Instant.now()
        val exp = now.plusSeconds(accessTtlSeconds)
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(userId: Int): String {
        val now = Instant.now()
        val exp = now.plusSeconds(refreshTtlSeconds)
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .audience().add("refresh").and()
            .signWith(key)
            .compact()
    }

    fun parseSubject(token: String): String? = runCatching {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
    }.getOrNull()

    fun validate(token: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token)
            .payload.expiration.after(Date())
    }.getOrDefault(false)

    fun validateRefresh(refreshToken: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(refreshToken)
            .payload.let { claims ->
                claims.audience.contains("refresh") &&
                        claims.expiration.after(Date())
            }
    }.getOrDefault(false)
}