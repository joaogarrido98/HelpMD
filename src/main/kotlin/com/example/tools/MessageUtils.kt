package com.example.tools

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization

object MessageUtils {
    /**
     * get env variables for email
     */
    private val fromEmail: String = System.getenv("FROM_EMAIL")
    private val apiKey: String = System.getenv("SENDGRID_KEY")

    /**
     * create an email for the new account password
     * @param email holds the patient email
     * @param code holds the password
     */
    fun sendRegistrationEmail(email: String, code: String) {
        val mail = Mail()
        val from = Email()
        val subject = "Account confirmation for HelpMD"
        val personalization = Personalization()
        val to = Email()
        from.email = fromEmail
        mail.setFrom(from)
        mail.subject = subject
        to.email = email
        personalization.addTo(to)
        personalization.subject = subject
        personalization.addDynamicTemplateData("code", code)
        mail.addPersonalization(personalization)
        val content = Content()
        content.type = "text/html"
        content.value = "HelpMD"
        mail.addContent(content)
        mail.setTemplateId("d-4bad812585644ceb9cbcb6db4b51dea3")
        sendEmail(mail)
    }

    /**
     * create an email for the recovery password
     * @param email holds the patient email
     * @param password holds the password
     */
    fun sendRecoverEmail(email: String, password: String) {
        val subject = "Recover Password for HelpMD"
        val content =
            Content(
                "text/plain",
                "This is your new password, " +
                        "please change immediately after logging in\n\n${password}"
            )
        val mail = Mail(Email(fromEmail), subject, Email(email), content)
        sendEmail(mail)
    }

    /**
     * send the email built before
     */
    private fun sendEmail(mail: Mail) {
        val sg = SendGrid(apiKey)
        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()
        sg.api(request)
    }
}