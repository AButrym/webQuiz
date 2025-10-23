package engine

import engine.common.logger
import engine.model.CreateTestRequest
import engine.security.IsCreator
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/tests")
class TestController(
    private val testService: TestService
) {
    private val log = logger()

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Int): Test =
        testService.getTest(id)

    @GetMapping
    fun getAll(): List<Test> =
        testService.getAllTests()

    @IsCreator
    @PostMapping
    fun create(
        @RequestBody @Valid
        test: CreateTestRequest
    ): ResponseEntity<Test> {
        log.debug("Creating a new test")
        val test = testService.createTest(test).also {
            log.debug("Created test: {}", it)
        }

        val uri: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(test.id)
            .toUri()

        return ResponseEntity.created(uri).body(test)
    }
}