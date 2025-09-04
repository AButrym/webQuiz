package engine.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {
    fun getLoggedUserId(): Int =
        (SecurityContextHolder.getContext()
            .authentication.principal
                as UserDetailsImpl).id
}