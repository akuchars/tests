package akuchars.application.email.command

interface EmailSender {
	fun sendEmail(to: String, title: String, content: String)
}