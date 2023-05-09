import com.example.models.Prescriptions
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test


class PrescriptionsRoutesTesting {
    val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJwYXRpZW50X2lkIjoyLCJwYXRpZW50X2VtYWlsIjoiY3Nnb2dhcnJpZG9AZ21haWwuY29tIiwiaXNzIjoiSGVscE1EIn0.VQdO_n6AYMy4Aa8WS-XZQXsUL3co2GFusWyKY3ZnW_Y"
    val doctorJwt= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJkb2N0b3JfaWQiOjEsImlzcyI6IkhlbHBNRCJ9.nL1TUl5ubyAD0LPX9SI5oJU6scEyCKhky3UMbNNtEyc"

    @Test
    fun testGetRegular() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/prescriptions/regular") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testGetOnce() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/prescriptions/once") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testGetErrorType() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/prescriptions/nothing") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals("No prescriptions of this type", response.message)
    }

    @Test
    fun testGetPrescriptionsRecent() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/prescriptions/recent") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostAddPrescriptionSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/prescriptions/add") {
            contentType(ContentType.Application.Json)
            bearerAuth(doctorJwt)
            setBody(
                Prescriptions(patient_id = 1, prescription_type = "Pill", prescription_regular = true,
                prescription_medicine =  "Paracetamol", prescription_dosage = "30 pills a day", prescription_doctor =
                1)
            )
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostAddPrescriptionBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/prescriptions/add") {
            contentType(ContentType.Application.Json)
            bearerAuth(doctorJwt)
            setBody(Prescriptions(patient_id = 1, prescription_type = "Pill", prescription_regular = true,
                prescription_medicine =  "", prescription_dosage = "30 pills a day", prescription_doctor =
                1))
        }.body()
        assertEquals("Bad Request", response.message)
    }
}