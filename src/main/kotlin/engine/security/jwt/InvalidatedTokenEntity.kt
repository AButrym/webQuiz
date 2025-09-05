package engine.security.jwt

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity(name = "InvalidatedToken")
@Table(name = "INVALIDATED_TOKEN", indexes = [
    Index(name = "idx_invalidated_token", columnList = "tokenId, expirationTime")
])
class InvalidatedTokenEntity(
    @Id
    val tokenId: String,
    val expirationTime: Long
)