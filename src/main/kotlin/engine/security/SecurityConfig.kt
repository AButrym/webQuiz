package engine.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity, jwtAuthFilter: JwtAuthFilter): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .headers { it.frameOptions {c -> c.disable()} }
            .authorizeHttpRequests { auth ->
                auth.dispatcherTypeMatchers(
                    DispatcherType.ERROR,
                    DispatcherType.FORWARD
                ).permitAll()
                auth
                    .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/refresh-token").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()
                    //.requestMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}