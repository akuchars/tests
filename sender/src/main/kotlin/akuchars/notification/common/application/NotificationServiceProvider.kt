package akuchars.notification.common.application

import akuchars.notification.common.application.model.MultipleNotificationDataDto
import akuchars.notification.common.application.model.NotificationType
import org.springframework.stereotype.Service

@Service
internal class NotificationServiceProvider(
		private val notificationServices: List<NotificationService>
) {

	fun findNotificationServiceByType(type: NotificationType): NotificationService? =
			notificationServices.find { it.getType() == type }

	fun notifyMultiple(multipleNotifications: MultipleNotificationDataDto) {
		multipleNotifications.notificationDtos.forEach {
			findNotificationServiceByType(it.type)?.notify(it)
		}
	}
}