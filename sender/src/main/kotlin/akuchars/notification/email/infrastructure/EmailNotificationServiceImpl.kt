package akuchars.notification.email.infrastructure

import akuchars.notification.common.application.NotificationService
import akuchars.notification.common.application.model.NotificationData
import akuchars.notification.common.domain.NotificationType
import akuchars.notification.common.domain.NotificationType.EMAIL
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
internal class EmailNotificationServiceImpl(
		private val javaMailSender: JavaMailSender
) : NotificationService {

	override fun notify(notificationDataToSend: NotificationData) {
		val mail = javaMailSender.createMimeMessage()
		kotlin.runCatching {
			MimeMessageHelper(mail, true).apply {
				setTo(notificationDataToSend.to)
				setFrom("adamkucharski1994@gmail.com")
				setSubject(notificationDataToSend.title)
				setText(notificationDataToSend.content, true)
			}
		}.map { javaMailSender.send(mail) }
	}

	override fun getType(): NotificationType = EMAIL
}