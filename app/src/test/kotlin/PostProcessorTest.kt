import extension.PostProcessorExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(PostProcessorExtension::class)
class PostProcessorTest {

    private lateinit var helloService: HelloService

    @Test
    fun testService() {
        val result = helloService.sayHello()
        assertEquals("Hello, World!", result)
    }
}