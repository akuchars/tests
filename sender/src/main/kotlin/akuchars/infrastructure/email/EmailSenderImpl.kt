package akuchars.infrastructure.email

import akuchars.application.email.command.EmailSender
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailSenderImpl(
		private val javaMailSender: JavaMailSender
): EmailSender {

	override fun sendEmail(to: String, title: String, content: String) {
		val mail = javaMailSender.createMimeMessage()
		kotlin.runCatching {
			MimeMessageHelper(mail, true).apply {
				setTo(to)
				setFrom("adamkucharski1994@gmail.com")
				setSubject(title)
				setText(content, true)
			}
		}.map { javaMailSender.send(mail) }
	}
}