package engine.users

import engine.model.RegisterUserReq
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/api/register")
    fun register(@Valid @RequestBody req: RegisterUserReq) {
        log.info("Registering user: $req")
        userService.createUser(
            email = req.email,
            password = req.password
        )
    }
}