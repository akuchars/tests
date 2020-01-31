package akuchars.notification.common.application

import akuchars.notification.common.application.model.NotificationData
import akuchars.notification.common.domain.NotificationType

interface NotificationService {
	fun notify(notificationDataToSend: NotificationData)

	fun getType(): NotificationType
}