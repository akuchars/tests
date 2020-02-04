package akuchars.notification.email.infrastructure

import akuchars.notification.common.application.NotificationService
import akuchars.notification.common.application.model.NotificationDataDto
import akuchars.notification.common.application.model.NotificationType
import akuchars.notification.common.application.model.NotificationType.EMAIL
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
internal class EmailNotificationServiceImpl(
		private val javaMailSender: JavaMailSender
) : NotificationService {

	override fun notify(notificationDataDtoToSend: NotificationDataDto) {
		val mail = javaMailSender.createMimeMessage()
		kotlin.runCatching {
			MimeMessageHelper(mail, true).apply {
				setTo(notificationDataDtoToSend.to)
				setFrom("adamkucharski1994@gmail.com")
				setSubject(notificationDataDtoToSend.title)
				setText(notificationDataDtoToSend.content, true)
			}
		}.map { javaMailSender.send(mail) }
	}

	override fun getType(): NotificationType = EMAIL
}