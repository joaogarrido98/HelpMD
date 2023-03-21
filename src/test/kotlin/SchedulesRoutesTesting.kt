import com.example.models.AddBookingsRequest
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class SchedulesRoutesTesting {
    val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJwYXRpZW50X2lkIjoyLCJwYXRpZW50X2VtYWlsIjoiam9hby5tZWxvLmdhcnJpZG9AZ21haWwuY29tIiwiaXNzIjoiSGVscE1EIn0.AGQ1LR33hASn4gcXIW0DMfuSjMP8eEl2nHR1a8YJ9bg"
    val doctorJwt= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJkb2N0b3JfaWQiOjEsImlzcyI6IkhlbHBNRCJ9.nL1TUl5ubyAD0LPX9SI5oJU6scEyCKhky3UMbNNtEyc"

    @Test
    fun testGetSchedulesDoctor() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/schedules/1/3/2023-12-17T00:00:00") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testGetSchedulesDoctorUnable() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/schedules/1/3/2023-12-13") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals("Unable to get schedule", response.message)
    }

}