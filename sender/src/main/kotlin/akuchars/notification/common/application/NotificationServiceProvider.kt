package akuchars.notification.common.application

import akuchars.notification.common.domain.NotificationType
import org.springframework.stereotype.Service

@Service
class NotificationServiceProvider(
		private val notificationServices: List<NotificationService>
) {

	fun findNotificationServiceByType(type: NotificationType): NotificationService? =
			notificationServices.find { it.getType() == type }
}