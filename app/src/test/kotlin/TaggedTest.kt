import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class TaggedTest {

    @Test
    @Tag("regress")
    @Tag("sanity")
    fun firstTest() {
        assertTrue { true }
    }

    @MY_TEST_ANNOTATION
    fun secondTest() {
        assertTrue(true)
    }

    @Test
    @Tag("smoke")
    fun thirdTest() {
        assertTrue(true)
    }

    @Test
    @Tag("regress")
    fun fourthTest() {
        assertTrue(false)
    }

}