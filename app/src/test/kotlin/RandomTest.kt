import extension.RandomExtension
import extension.RandomInt
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertTrue

@ExtendWith(RandomExtension::class)
class RandomTest {

    @Test
    fun randomIntTest(@RandomInt(min = 1, max = 100) number: Int) {
        assertTrue(number in 1..100)
    }
}