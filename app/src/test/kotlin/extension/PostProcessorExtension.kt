package extension

import HelloService
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor

class PostProcessorExtension : TestInstancePostProcessor {
    override fun postProcessTestInstance(testInstance: Any, p1: ExtensionContext?) {
        val serviceField = testInstance::class.java.getDeclaredField("helloService")
        serviceField.isAccessible = true
        serviceField.set(testInstance, HelloService())
    }

}