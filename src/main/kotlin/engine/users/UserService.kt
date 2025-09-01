package engine.users

import engine.model.JwtTokensDto
import engine.model.entity.UserEntity
import engine.security.UserDetailsImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

typealias JwtToken = String

@Service
class UserService(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? =
        userRepo.findByEmail(username!!)?.let {
            UserDetailsImpl(
                it.id!!,
                it.email,
                it.passwordHash,
                emptyList()
            )
        }

    fun createUser(email: String, password: String) : JwtTokensDto {
        if (userRepo.existsByEmail(email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                "User with email $email already exists")
        }
        val user = userRepo.save(UserEntity(
            email = email,
            passwordHash = passwordEncoder.encode(password)))
        return JwtTokensDto(user.id!!, user.email, "fakeJwt", "fakeRefreshJwt")
    }

    fun login(email: String, password: String) : JwtTokensDto {
        val user = userRepo.findByEmail(email) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        TODO("check password")
        return JwtTokensDto(user.id!!, user.email, "fakeJwt", "fakeRefreshJwt")
    }

    fun getJwtToken() : JwtToken = "fakeJwt"
}