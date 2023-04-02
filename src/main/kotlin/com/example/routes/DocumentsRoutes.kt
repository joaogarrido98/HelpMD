package com.example.routes

import com.example.models.Patient
import com.example.models.ServerResponse
import com.example.services.DocumentsServices
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.io.IOException
import java.util.*

fun Route.documentsRoutes(documentsServices: DocumentsServices) {
    authenticate("patient-interaction") {
        post("documents/upload") {
            val patient = call.principal<Patient>()?.patient_id
            lateinit var fileName: String
            try {
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val name = part.originalFileName.toString()
                        fileName = "$patient-${UUID.randomUUID()}-$name"
                        val fileBytes = part.streamProvider().readBytes()
                        File("uploads/$fileName").writeBytes(fileBytes)
                    }
                    part.dispose()
                }
                call.respond(ServerResponse(true, "File Uploaded"))
            } catch (e: Exception) {
                call.respond(ServerResponse(false, "Unable to upload file"))
            }


        }
    }
}