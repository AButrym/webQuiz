package engine.model

data class JwtTokensDto(
    val accessToken: String,
    val refreshToken: String
)
