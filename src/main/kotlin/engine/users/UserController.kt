package engine.users

import engine.common.logger
import engine.model.JwtTokenDto
import engine.model.JwtTokensDto
import engine.model.RefreshTokenReq
import engine.model.RegisterUserReq
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    private val log = logger()

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/register")
    fun register(@Valid @RequestBody req: RegisterUserReq): JwtTokensDto =
        userService.createUser(
            email = req.email,
            password = req.password
        ).also { log.debug("Register user: {}", req) }

    @PostMapping("/api/login")
    fun login(@Valid @RequestBody req: RegisterUserReq): JwtTokensDto =
        userService.login(
            email = req.email,
            password = req.password
        ).also { log.debug("Login user: {}", req) }

    @PostMapping("/api/refresh-token")
    fun refresh(@Valid @RequestBody req: RefreshTokenReq): JwtTokenDto =
        userService.refreshToken(req.refreshToken)
            .also { log.debug("Refresh token: {}", req) }
}