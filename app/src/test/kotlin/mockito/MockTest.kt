package mockito

import ArithmeticCalculatorImpl
import DivideCalculator
import MathCalculator
import MultiplyCalculator
import SubtractCalculator
import SumCalculator
import TrigonometryCalculator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import org.mockito.kotlin.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class TestCalculator {

//    @Test
//    fun testCalcWithoutMock() {
//        val calculator = MathCalculator(
//            arithmeticCalculator = ArithmeticCalculatorImpl(
//                sumCalculator = SumCalculator(),
//                subtractCalculator = SubtractCalculator(),
//                multiplyCalculator = MultiplyCalculator(),
//                divideCalculator = DivideCalculator()
//            ),
//            trigonometryCalculator = TrigonometryCalculator()
//        )
//        assertEquals(0.5, calculator.calculateSinOfSum(15, 15), absoluteTolerance = 0.0005)
//    }
//
//    @Test
//    fun testCalcWithMock() {
//        println("@@@ testCalcWithMock()")
//        val sumCalc = mock(SumCalculator::class.java)
//        `when`(sumCalc.sum(anyInt(), anyInt())).thenReturn(30)
//
//        val calculator = MathCalculator(
//            arithmeticCalculator = ArithmeticCalculatorImpl(
//                sumCalculator = sumCalc,
//                subtractCalculator = SubtractCalculator(),
//                multiplyCalculator = MultiplyCalculator(),
//                divideCalculator = DivideCalculator()
//            ),
//            trigonometryCalculator = TrigonometryCalculator()
//        )
//        assertEquals(0.5, calculator.calculateSinOfSum(15, 15), absoluteTolerance = 0.0005)
//        verify(sumCalc).sum(15, 15).times(1)
//    }
//
//    @Mock
//    lateinit var annotatedSumCalculator: SumCalculator
//
//    @Test
//    fun testWithAnnotatedMock() {
//        println("@@@ testWithAnnotatedMock()")
//        `when`(annotatedSumCalculator.sum(anyInt(), anyInt())).thenReturn(30)
//        val calculator = MathCalculator(
//            arithmeticCalculator = ArithmeticCalculatorImpl(
//                sumCalculator = annotatedSumCalculator,
//                subtractCalculator = SubtractCalculator(),
//                multiplyCalculator = MultiplyCalculator(),
//                divideCalculator = DivideCalculator()
//            ),
//            trigonometryCalculator = TrigonometryCalculator()
//        )
//        assertEquals(0.5, calculator.calculateSinOfSum(15, 15), absoluteTolerance = 0.0005)
////        verify(annotatedSumCalculator).sum(15, 15).times(1)
//
//    }
//
//    @Test
//    fun testMockWithException() {
//        println("@@@ testMockWithException()")
//        val sumCalc = mock(SumCalculator::class.java)
//        `when`(sumCalc.sum(anyInt(), anyInt())).thenThrow(IllegalArgumentException::class.java)
//        val calculator = MathCalculator(
//            arithmeticCalculator = ArithmeticCalculatorImpl(
//                sumCalculator = sumCalc,
//                subtractCalculator = SubtractCalculator(),
//                multiplyCalculator = MultiplyCalculator(),
//                divideCalculator = DivideCalculator()
//            ),
//            trigonometryCalculator = TrigonometryCalculator()
//        )
//        assertThrows<IllegalArgumentException> {
//            calculator.calculateSinOfSum(15, 15)
//        }
//    }

//    @Test
//    fun testWithArgThatError() {
//        println("@@@ testWithArgThatError()")
//        val sumCalculator = mock(SumCalculator::class.java)
//        `when`(sumCalculator.sum(argThat { it % 2 == 0 }, argThat { it % 2 == 0 })).thenReturn(20)
//        `when`(sumCalculator.sum(argThat { it % 2 == 1 }, argThat { it % 2 == 1 })).thenReturn(1111)
//
//        assertEquals(20, sumCalculator.sum(2, 2))
//    }
//
//    @Test
//    fun testWithKotlinMock() {
//        println("@@@ testWithKotlinMock()")
//        val calculator: SumCalculator = org.mockito.kotlin.mock()
//        whenever(calculator.sum(org.mockito.kotlin.argThat { it % 2 == 0 }, org.mockito.kotlin.argThat { it % 2 == 0 })).thenReturn(20)
////        whenever(calculator.sum(argThat { it % 2 == 1 }, argThat { it % 2 == 1 })).thenReturn(1111)
//        assertEquals(20, calculator.sum(2, 2))
//    }

    @Test
    fun testDoAnswer() {
        println("@@@ testDoAnswer()")
        val calculator: SumCalculator = mock()
        doAnswer {
            val a = it.getArgument(0) as Int
            val b = it.getArgument(1) as Int
            if (a % 2 == 0 && b % 2 == 0) return@doAnswer 20
            else if (a % 2 == 1 && b % 2 == 1) return@doAnswer 1111
            else return@doAnswer 0
        }.`when`(calculator).sum(anyInt(), anyInt())

        assertEquals(20, calculator.sum(10, 10))
        assertEquals(1111, calculator.sum(1, 11))
        assertEquals(0, calculator.sum(1, 2))
    }
}