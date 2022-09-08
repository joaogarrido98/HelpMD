import com.example.tools.ProjectUtils
import org.junit.Test
import kotlin.test.assertEquals

class ProjectUtilsTest {

    @Test
    fun `Test Random generated code length`() {
        val expectedLength: Int = 30
        val receivedLength: Int = ProjectUtils.generateRandomCode().length
        assertEquals(expectedLength, receivedLength)
    }
}