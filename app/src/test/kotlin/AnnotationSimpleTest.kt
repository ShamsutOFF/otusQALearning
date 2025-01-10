import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnnotationSimpleTest {

    companion object {

        @JvmStatic
        @BeforeAll
        fun beforeAllMethods() {
            println("@@@ before all methods")
        }

        @JvmStatic
        @AfterAll
        fun afterAllMethods() {
            println("@@@ after all methods")
        }
    }

    @BeforeEach
    fun beforeEachMethods() {
        println("@@@ before each methods")
    }

    @AfterEach
    fun afterEachMethods() {
        println("@@@ after each methods")
    }

    @Test
    fun `simple test with test annotation`() {
        println("@@@ simple test with test annotation")
        assertTrue(true)
    }

    @Test
    fun `another simple test with test annotation`() {
        println("@@@ another simple test with test annotation")
        assertEquals(2, 1 + 1)
    }

    @RepeatedTest(5)
    fun repeatedTest() {
        println("@@@ repeated test")
        val number = (1..200).random()
        println("@@@ number = $number")
        assertTrue(number in 1..100)

    }

    @Test
    fun testWithException() {
        println("@@@ testWithException()")
        fun doSomeWithException() {
            println("@@@ doSomeWithException()")
            throw IndexOutOfBoundsException()
        }

        assertThrows<IndexOutOfBoundsException> {
            println("@@@ assertThrows<IndexOutOfBoundsException>")
            doSomeWithException()
        }

    }

    @Disabled("this test is disabled")
    @Test
    fun disabledTest() {
        println("@@@ disabled test")
        assertTrue(true)
    }

}