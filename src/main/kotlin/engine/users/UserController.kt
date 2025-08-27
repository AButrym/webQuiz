package engine.users

import engine.model.RegisterUserReq
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/api/register")
    fun register(@RequestBody req: RegisterUserReq) {
        userService.createUser(
            email = req.email,
            password = req.password
        )
    }
}