package engine.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role {
    ADMIN, CREATOR, USER
}

val Role.asGrantedAuthority: GrantedAuthority
    get() = SimpleGrantedAuthority("ROLE_$name")
