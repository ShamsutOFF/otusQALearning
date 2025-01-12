package extension

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import java.sql.SQLException

@Target(AnnotationTarget.FUNCTION)  // Эта аннотация может быть применена только к методам (функциям).
@ExtendWith(RetrySQLExceptionExtension::class)  // Указывает, что эта аннотация будет использовать расширение RetrySQLExceptionExtension для обработки тестов.
annotation class RetrySQLException(val maxRetries: Int = 3) // Это сама аннотация, которая принимает параметр maxRetries со значением по умолчанию 3. Этот параметр определяет, сколько раз тест будет перезапущен, если он выбросит SQLException.

class RetrySQLExceptionExtension : TestExecutionExceptionHandler {  // TestExecutionExceptionHandler: Это интерфейс, который позволяет обрабатывать исключения, возникающие во время выполнения тестов.
    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {    // Метод, который вызывается, когда тест выбрасывает исключение.
        val testMethod = context.testMethod.get()   // Получает метод, который вызвал исключение.
        val annotation = testMethod.getAnnotation(RetrySQLException::class.java)    // Получает аннотацию @RetrySQLException, если она есть у метода.
        if (annotation != null && throwable is SQLException) {
            val store = context.getStore(ExtensionContext.Namespace.GLOBAL) // Получает хранилище, где можно сохранять данные между вызовами.
            val retries = store.getOrDefault(context.uniqueId, Int::class.java, annotation.maxRetries)  // Получает количество оставшихся попыток из хранилища или использует значение по умолчанию из аннотации.

            if (retries < 1) throw throwable    // Если попытки закончились, выбрасываем исключение.

            store.put(context.uniqueId, retries - 1)    // Уменьшаем количество оставшихся попыток и сохраняем в хранилище.
            println("@@@ Retrying ${testMethod.name} with $retries retries")
            context.testInstanceLifecycle.ifPresent { lifecycle ->  // Проверяем, какой жизненный цикл у тестового класса (PER_CLASS или PER_METHOD) и в зависимости от этого вызываем тестовый метод снова.
                try {
                    if (lifecycle == TestInstance.Lifecycle.PER_CLASS)
                        context.requiredTestMethod.invoke(context.requiredTestInstance)
                    else
                        context.requiredTestMethod.invoke(context.requiredTestClass.getConstructor().newInstance())
                } catch (e: Exception) {
                    handleTestExecutionException(context, e.cause ?: e)
                }

            }
        } else throw throwable
    }

}