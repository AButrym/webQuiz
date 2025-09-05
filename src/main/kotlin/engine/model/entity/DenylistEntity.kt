package engine.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(name = "TokenIdEntity", indexes = [
    Index(name = "idx_tokenidentity_tokenid", columnList = "tokenId, expirationTime")
])
class DenylistEntity(
    @Id
    val tokenId: String,
    val expirationTime: Long
)