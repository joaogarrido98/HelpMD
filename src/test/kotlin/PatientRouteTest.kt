import com.example.models.PatientLoginRequest
import com.example.models.ServerResponse
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PatientRouteTest {
    /**
     * login not valid test
     * give empty patient email and password
     */
    @Test
    fun testLoginNotValidEmpty() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest("", ""))
        }
        assertEquals("not valid", response.bodyAsText())
    }

    /**
     * login valid test
     * give invalid patient email and password
     */
    @Test
    fun testLoginValid() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest("dwadw", "dawdaw"))
        }
        assertEquals("valid", response.bodyAsText())
    }

    /**
     * login bad request test
     * dont request the correct format
     */
    @Test
    fun testLoginBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(ServerResponse(false, "heao"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}