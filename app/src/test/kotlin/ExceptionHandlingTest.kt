import extension.RetrySQLException
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.net.ConnectException
import java.sql.SQLException
import kotlin.random.Random
import kotlin.test.assertTrue

class ExceptionHandlingTest {

    @Test
    @RetrySQLException(maxRetries = 3)
    fun testThrowsSQLException() {
        println("@@@ Running testThrowsSQLException()")
        sleep(1000)
        if (Random.nextInt(from = 0, until = 100) < 70)
            throw SQLException("@@@ test Throws SQLException()!!!! ")
    }

    @Test
    @RetrySQLException()
    fun okTest() {
        println("@@@ Running okTest()")
        assertTrue(true)
    }

    @Test
    fun testThrowsConnectException() {
        println("@@@ Running testThrowsConnectException()")
        sleep(1000)
        throw ConnectException("@@@ test Throws ConnectException()!!!! ")
    }
}