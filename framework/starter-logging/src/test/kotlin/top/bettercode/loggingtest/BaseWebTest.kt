package top.bettercode.loggingtest

import top.bettercode.logging.RequestLoggingFilter
import top.bettercode.logging.RequestLoggingProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

/**
 * mockMvc 基础测试类
 *
 * @author Peter Wu
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(properties = ["logging.level.root=info", "spring.autoconfigure.exclude=top.bettercode.logging.websocket.WebsocketConfiguration"])
abstract class BaseWebTest {

    @Autowired
    private lateinit var context: WebApplicationContext
    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        val properties = RequestLoggingProperties()
        properties.isIncludeRequestBody = true
        properties.isIncludeResponseBody = true
        properties.isFormat = true
        properties.encryptHeaders = arrayOf("token")
        properties.encryptParameters = arrayOf("password")
        mockMvc = webAppContextSetup(context).addFilter<DefaultMockMvcBuilder>(
            RequestLoggingFilter(
                properties,
                emptyList()
            )
        ).build()
    }

}
