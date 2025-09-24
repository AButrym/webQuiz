package engine.model.entity

import engine.security.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.proxy.HibernateProxy

@Entity
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int? = null,
    val email: String,
    val passwordHash: String,
    @Enumerated(EnumType.STRING)
    val role: Role
) : JpaBase()