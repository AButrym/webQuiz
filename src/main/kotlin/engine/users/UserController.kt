package engine.users

import engine.model.JwtTokensDto
import engine.model.RegisterUserReq
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/register")
    fun register(@Valid @RequestBody req: RegisterUserReq): JwtTokensDto {
        log.info("Registering user: $req")
        return userService.createUser(
            email = req.email,
            password = req.password
        )
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/login")
    fun login(@Valid @RequestBody req: RegisterUserReq): JwtTokensDto {
        log.info("Login user: $req")
        return userService.createUser(
            email = req.email,
            password = req.password
        )
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/get-jwt-token")
    fun getJwtToken(): JwtToken = userService.getJwtToken()

}