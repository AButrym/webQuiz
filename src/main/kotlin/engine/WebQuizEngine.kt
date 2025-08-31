package engine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class WebQuizEngine

fun main(args: Array<String>) {
    runApplication<WebQuizEngine>(*args)
}
