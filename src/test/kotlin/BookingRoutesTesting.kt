import com.example.models.Bookings
import com.example.models.ServerResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.Test

class BookingRoutesTesting {
    val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxwbWQiLCJwYXRpZW50X2lkIjoyLCJwYXRpZW50X2VtYWlsIjoiam9hby5tZWxvLmdhcnJpZG9AZ21haWwuY29tIiwiaXNzIjoiSGVscE1EIn0.AGQ1LR33hASn4gcXIW0DMfuSjMP8eEl2nHR1a8YJ9bg"

    @Test
    fun testGetBookingsPatient() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/bookings/patient") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testGetBookingsPatientPrevious() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.get("/bookings/previous") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostBookingAddSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/bookings/add") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
            setBody(Bookings(booking_doctor = 1, booking_patient = 2, booking_date_start =
            "2023-03-20T19:00:00", booking_date_end =  "2023-03-20T20:00:00"))
        }.body()
        assertEquals(true, response.success)
    }

    @Test
    fun testPostBookingAddExists() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/bookings/add") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
            setBody(Bookings(booking_doctor = 1, booking_patient = 2, booking_date_start =
            "2023-03-19T19:00:00", booking_date_end =  "2023-03-19T19:00:00"))
        }.body()
        assertEquals("Booking already exists", response.message)
    }

    @Test
    fun testPostBookingAddBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/bookings/add") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
            setBody(Bookings(booking_doctor = 1, booking_patient = 2, booking_date_start =
            "2023-03-19T19:00:00", booking_date_end =  ""))
        }.body()
        assertEquals("Bad Request", response.message)
    }

    @Test
    fun testPostBookingDelete() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: ServerResponse = client.post("/bookings/delete/1") {
            contentType(ContentType.Application.Json)
            bearerAuth(jwt)
        }.body()
        assertEquals(true, response.success)
    }


}