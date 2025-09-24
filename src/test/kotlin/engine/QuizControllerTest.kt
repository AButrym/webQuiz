package engine

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@SpringBootTest
class QuizControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @WithMockUser
    @Test
    fun `forbidden for role USER`() {
        mockMvc.get("/api/quizzes/test")
            .andExpect {
                status { isForbidden() }
            }
    }

    @WithMockUser(roles = ["CREATOR"])
    @Test
    fun `allowed for role CREATOR`() {
        mockMvc.get("/api/quizzes/test")
            .andExpect {
                status { isOk() }
            }
    }
}