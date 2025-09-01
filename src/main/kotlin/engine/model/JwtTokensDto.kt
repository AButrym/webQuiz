package engine.model

data class JwtTokensDto(
    val id: Int,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
