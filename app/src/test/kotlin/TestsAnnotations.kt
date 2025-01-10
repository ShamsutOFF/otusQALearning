import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Target(AnnotationTarget.FUNCTION)
@Tag("regress")
@Tag("sanity")
@Tag("dev")
@Test
annotation class MY_TEST_ANNOTATION
