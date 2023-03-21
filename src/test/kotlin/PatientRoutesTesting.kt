import com.example.models.PatientLoginRequest
import com.example.models.PatientRegisterRequest
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class PatientRoutesTesting {
    @Test
    fun testPostLoginSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest(patient_email = "joao.melo.garrido@gmail.com", patient_password = "Mouros123*"))
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostLoginFailed() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest(patient_email = "joao.melo.garrido@gmail.com", patient_password = "ouros123*"))
        }.body()
        assertEquals(false, response.success)
    }

    @Test
    fun testPostLoginBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest(patient_email = "joao.melo.garrido@gmail.com", patient_password = ""))
        }.body()
        assertEquals(false, response.success)
    }

    @Test
    fun testPostLoginNotActive() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/login") {
            contentType(ContentType.Application.Json)
            setBody(PatientLoginRequest(patient_email = "csgogarrido@gmail.com", patient_password = "Mouros123*"))
        }.body()
        assertEquals("Account not active", response.message)
    }

    @Test
    fun testPostRegisterSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/register") {
            contentType(ContentType.Application.Json)
            setBody(PatientRegisterRequest("joao.pmg@hotmail.com", "Joao Pedro", "1998-12-17", 100, 180,
                "Male", "Mouros123*", true))
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostRegisterBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/register") {
            contentType(ContentType.Application.Json)
            setBody(PatientRegisterRequest("joao.melo.garrido@hotmail.com", "Joao Pedro", "1998-12-17", 100, 180,
                "Male", "", true))
        }.body()
        assertEquals("Bad Request", response.message)
    }

    @Test
    fun testPostRegisterEmailInUse() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/register") {
            contentType(ContentType.Application.Json)
            setBody(PatientRegisterRequest("joao.melo.garrido@gmail.com", "Joao Pedro", "1998-12-17", 100, 180,
                "Male", "Mouros123*", true))
        }.body()
        assertEquals("Email already in use", response.message)
    }


}