package engine.model

data class JwtTokensDto(
    val accessToken: String,
    val refreshToken: String
)

data class JwtTokenDto(
    val accessToken: String
)