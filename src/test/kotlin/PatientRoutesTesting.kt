import com.example.models.PatientLoginRequest
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class PatientRoutesTesting {
    @Test
    fun testPostLoginSuccess() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response : ServerResponse = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest(patient_email = "joao.melo.garrido@gmail.com", patient_password = "Mouros123*"))
        }.body()
        assertEquals(true, response.success)
    }
}