package engine.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class RegisterUserReq(
    @field:Email(message = "Email must be valid", regexp = "^(.+)@([^.]+)\\.([^.]+)$")
    val email: String,
    @field:Size(min = 5, message = "Password must be at least 5 characters long")
    val password: String
)
