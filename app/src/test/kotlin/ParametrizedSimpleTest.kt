import data.Day
import data.isWeekend
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ParametrizedSimpleTest {

    @ParameterizedTest
    @ValueSource(strings = ["OTUS", "radar", "level", "hello"])
    fun checkIsPalindromeTest(word: String) {
        assertEquals(word, word.reversed(), "Word is not a palindrome")
    }

    @ParameterizedTest
    @EnumSource(Day::class, names = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"])
    fun checkIfDayIsWeekend(day: Day) {
        assertTrue { day.isWeekend() }
//        assertTrue(day.isWeekend())
    }


    @ParameterizedTest
    @EnumSource(Day::class, names = ["SUNDAY", "SATURDAY"], mode = EnumSource.Mode.EXCLUDE)
    fun checkIfDayIsNotWeekend(day: Day) {
        assertFalse(day.isWeekend())
    }

    @ParameterizedTest
    @CsvSource("1,1,2", "2,2,4", "3,3,6", "4,1,3")
    fun checkSumFromCsv(a: Int, b: Int, expected: Int) {
        assertEquals(expected, a + b)
    }

    @ParameterizedTest
    @MethodSource("provideNumbers")
    fun checkSumFromMethod(a: Int, b: Int, expected: Int) {
        assertEquals(expected, a + b)
    }

    companion object {

        @JvmStatic
        fun provideNumbers(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(1, 1, 2),
                Arguments.of(2, 2, 4),
                Arguments.of(3, 3, 6),
                Arguments.of(4, 1, 3)
            )
        }
    }
}