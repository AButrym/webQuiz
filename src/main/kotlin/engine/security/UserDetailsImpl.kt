package engine.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserDetailsImpl(
    val id: Int,
    email: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(email, password, authorities)