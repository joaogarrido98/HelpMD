import com.example.models.AddBookingsRequest
import com.example.models.AddPatientHistoryRequest
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class PatientHistoryRoutesTesting {
    val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJwYXRpZW50X2lkIjoyLCJwYXRpZW50X2VtYWlsIjoiam9hby5tZWxvLmdhcnJpZG9AZ21haWwuY29tIiwiaXNzIjoiSGVscE1EIn0.AGQ1LR33hASn4gcXIW0DMfuSjMP8eEl2nHR1a8YJ9bg"

    @Test
    fun testGetPatientHistory() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/patient/history") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostPatientHistorySuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/history") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
            setBody(AddPatientHistoryRequest(patient_blood = "B+", patient_diseases = "This;That;", patient_vaccines
            = "That;Those;", patient_lifestyle = "Smoking;", patient_family = "HeartDisease;", patient_allergies =
            "Covid;" ))
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostPatientHistoryBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/patient/history") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
            setBody(AddPatientHistoryRequest(patient_blood = "", patient_diseases = "This;That;", patient_vaccines
            = "That;Those;", patient_lifestyle = "Smoking;", patient_family = "HeartDisease;", patient_allergies =
            "Covid;" ))
        }.body()
        assertEquals("Bad request", response.message)
    }


}