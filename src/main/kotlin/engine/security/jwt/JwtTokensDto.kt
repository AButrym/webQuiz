package engine.security.jwt

data class JwtTokensDto(
    val accessToken: String,
    val refreshToken: String
)