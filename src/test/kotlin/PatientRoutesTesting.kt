import com.example.models.Patient
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
            setBody(Patient(patient_email = "joao.melo.garrido@gmail.com", patient_password = "Mouros123*"))
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
            setBody(Patient(patient_email = "joao.melo.garrido@gmail.com", patient_password = "ouros123*"))
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
            setBody(Patient(patient_email = "joao.melo.garrido@gmail.com", patient_password = ""))
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
            setBody(Patient(patient_email = "csgogarrido@gmail.com", patient_password = "Mouros123*"))
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
            setBody(Patient(patient_email = "joao.melo.garrido@hotmail.com", patient_name = "Joao Pedro", patient_dob
            =  "1998-12-17", patient_weight = 100,
                patient_height = 180,
                patient_gender = "Male", patient_password = "Mouros123*", patient_deaf = true))
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
            setBody(Patient(patient_email = "joao.melo.garrido@hotmail.com", patient_name = "Joao Pedro", patient_dob
            =  "1998-12-17", patient_weight = 100,
                patient_height = 180,
                patient_gender = "Male", patient_password = "", patient_deaf = true))
        }.body()
        assertEquals("Bad Request", response.message)
    }

    @Test
    fun testPostRegisterEmailInUse() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }        }
        val response: ServerResponse = client.post("/patient/register") {
            contentType(ContentType.Application.Json)
            setBody(Patient(patient_email = "joao.melo.garrido@gmail.com", patient_name = "Joao Pedro", patient_dob =
            "1998-12-17", patient_weight = 100, patient_height =  180,
                patient_gender = "Male", patient_password = "Mouros123*", patient_deaf =  true))
        }.body()
        assertEquals("Email already in use", response.message)
    }


}