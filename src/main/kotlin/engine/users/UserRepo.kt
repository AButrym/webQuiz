package engine.users

import engine.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<UserEntity, Int> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}