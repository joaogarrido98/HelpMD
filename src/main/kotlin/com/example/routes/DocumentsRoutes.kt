package com.example.routes

import com.example.models.Patient
import com.example.services.DocumentsServices
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.*

fun Route.documentsRoutes(documentsServices : DocumentsServices) {

    authenticate("patient-interaction") {
        post("documents/upload") {
            val patient = call.principal<Patient>()?.patient_id
            val multipartData = call.receiveMultipart()
            var fileDescription = ""
            var fileName = ""
            val newFileName = "${patient}-${UUID.randomUUID()}"
            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }

                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        File("uploads/$newFileName").writeBytes(fileBytes)
                    }

                    else -> {}
                }
                part.dispose()
            }
            call.respondText("$fileDescription is uploaded to 'uploads/$newFileName'")
        }
    }
}