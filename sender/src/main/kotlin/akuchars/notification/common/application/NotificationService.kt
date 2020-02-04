package akuchars.notification.common.application

import akuchars.notification.common.application.model.NotificationDataDto
import akuchars.notification.common.application.model.NotificationType

internal interface NotificationService {

	fun notify(notificationDataDtoToSend: NotificationDataDto)

	fun getType(): NotificationType
}