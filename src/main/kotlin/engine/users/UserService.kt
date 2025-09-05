package engine.users

import engine.model.JwtTokensDto
import engine.model.entity.UserEntity
import engine.security.UserDetailsImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class UserService(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: engine.security.JwtService
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepo.findByEmail(username)?.let {
            UserDetailsImpl(
                it.id!!,
                it.email,
                it.passwordHash,
                emptyList()
            )
        } ?: throw UsernameNotFoundException("User not found")

    private fun createTokensForId(id: Int) : JwtTokensDto {
        val refresh = jwtService.generateRefreshToken(id)
        val tokenId = jwtService.parseId(refresh)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        val access = jwtService.generateAccessToken(id, tokenId)
        return JwtTokensDto(access, refresh)
    }

    fun createUser(email: String, password: String) : JwtTokensDto {
        if (userRepo.existsByEmail(email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT,
                "User with email $email already exists")
        }
        val (id) = userRepo.save(UserEntity(
            email = email,
            passwordHash = passwordEncoder.encode(password)))

        return createTokensForId(id)
    }

    fun login(email: String, password: String) : JwtTokensDto {
        val user = userRepo.findByEmail(email) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
        return createTokensForId(user.id!!)
    }

    fun refreshToken(refreshToken: String): JwtTokensDto {
        if (!jwtService.validateRefresh(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }
        jwtService.invalidate(refreshToken)
        val userId = jwtService.parseSubject(refreshToken)
            ?.toIntOrNull()
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

        return createTokensForId(userId)
    }

    fun logout(refreshToken: String) {
        if (!jwtService.validateRefresh(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }
        jwtService.invalidate(refreshToken)
    }
}