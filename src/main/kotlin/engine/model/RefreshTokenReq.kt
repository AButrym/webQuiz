package engine.model

import jakarta.validation.constraints.NotBlank

data class RefreshTokenReq(
    @field:NotBlank
    val refreshToken: String
)