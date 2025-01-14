class SumCalculator {
    fun sum(a: Int, b: Int): Int {
        return a + b
    }
}

class SubtractCalculator {
    fun subtract(a: Int, b: Int): Int {
        return a - b
    }
}

class MultiplyCalculator {
    fun multiply(a: Int, b: Int): Int {
        return a * b
    }
}

class DivideCalculator {
    fun divide(a: Int, b: Int): Int {
        return a / b
    }
}

interface ArithmeticCalculator {
    fun sum(a: Int, b: Int): Int
    fun subtract(a: Int, b: Int): Int
    fun multiply(a: Int, b: Int): Int
    fun divide(a: Int, b: Int): Int
}

class ArithmeticCalculatorImpl(
    private val sumCalculator: SumCalculator,
    private val subtractCalculator: SubtractCalculator,
    private val multiplyCalculator: MultiplyCalculator,
    private val divideCalculator: DivideCalculator
) : ArithmeticCalculator {
    override fun sum(a: Int, b: Int): Int = sumCalculator.sum(a, b)
    override fun subtract(a: Int, b: Int): Int = subtractCalculator.subtract(a, b)
    override fun multiply(a: Int, b: Int): Int = multiplyCalculator.multiply(a, b)
    override fun divide(a: Int, b: Int): Int = divideCalculator.divide(a, b)
}

class TrigonometryCalculator {

    fun sin(a: Double): Double {
        return Math.sin(a)
    }

    fun cos(a: Double): Double {
        return Math.cos(a)
    }

    fun tan(a: Double): Double {
        return Math.tan(a)
    }

    fun ctg(a: Double): Double {
        return 1 / Math.tan(a)
    }
}

class MathCalculator(
    private val arithmeticCalculator: ArithmeticCalculator,
    private val trigonometryCalculator: TrigonometryCalculator
) {
    fun calculateSinOfSum(a: Int, b: Int): Double {
        val sum = arithmeticCalculator.sum(a, b)
        val angle = radiusToDegrees(sum.toDouble())
        return trigonometryCalculator.sin(angle)
    }

    private fun radiusToDegrees(radius: Double): Double {
        return radius * Math.PI / 180
    }
}