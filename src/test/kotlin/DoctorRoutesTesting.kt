import com.example.models.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class DoctorRoutesTesting {
    @Test
    fun testPostRegisterSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/doctor/register") {
            contentType(ContentType.Application.Json)
            setBody(DoctorRegisterRequest(doctor_email = "pedro@gmail.com", doctor_password = "thispassword",
                doctor_name = "Pedro Garrido", doctor_sign_language = false))
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
        val response: ServerResponse = client.post("/doctor/register") {
            contentType(ContentType.Application.Json)
            setBody(DoctorRegisterRequest(doctor_email = "", doctor_password = "thispassword",
                doctor_name = "Pedro Garrido", doctor_sign_language = false))
        }.body()
        assertEquals("Bad Request", response.message)
    }


    @Test
    fun testPostLoginSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/doctor/login") {
            contentType(ContentType.Application.Json)
            setBody(DoctorLoginRequest(doctor_email = "pedro@gmail.com", doctor_password = "thispassword"))
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostLoginBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/doctor/login") {
            contentType(ContentType.Application.Json)
            setBody(DoctorLoginRequest(doctor_email = "", doctor_password = "thispassword"))
        }.body()
        assertEquals("Bad Request", response.message)
    }

    @Test
    fun testPostLoginEmailPasswordWrong() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/doctor/login") {
            contentType(ContentType.Application.Json)
            setBody(DoctorLoginRequest(doctor_email = "pedro@gmail.com", doctor_password = "dwa"))
        }.body()
        assertEquals("Email or password incorrect", response.message)
    }
}