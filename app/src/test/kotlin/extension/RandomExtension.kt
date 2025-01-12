package extension

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RandomInt(val min: Int = Integer.MIN_VALUE, val max: Int = Integer.MAX_VALUE - 1)

class RandomExtension : ParameterResolver {
    override fun supportsParameter(parameterContext: ParameterContext, p1: ExtensionContext?): Boolean {
        val bool = parameterContext.parameter.type == Int::class.java &&
                parameterContext.isAnnotated(RandomInt::class.java)
        println("@@@@@ supportsParameter() return = $bool")
        return bool
    }

    override fun resolveParameter(parameterContext: ParameterContext, p1: ExtensionContext?): Any {
        println("@@@@@ resolveParameter()")
        val numberAnnotation = parameterContext.findAnnotation(RandomInt::class.java).get()
        val min = numberAnnotation.min
        val max = numberAnnotation.max
        return (min..max).random()
    }

}
