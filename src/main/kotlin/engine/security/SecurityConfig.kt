package engine.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.authorizeHttpRequests { auth ->
            auth.dispatcherTypeMatchers(
                DispatcherType.ERROR,
                DispatcherType.FORWARD
            ).permitAll()
            auth
                //.requestMatchers(HttpMethod.POST, "/error/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
        }
//            .csrf{ it.ignoringRequestMatchers(toH2Console()) }
            .csrf { it.disable() }

            .headers { it.frameOptions { it.disable() } }

            .httpBasic {}
            .build()

}