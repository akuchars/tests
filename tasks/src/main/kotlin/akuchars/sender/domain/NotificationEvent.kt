package akuchars.sender.domain

import akuchars.motivation.domain.model.NotificationType

class NotificationEvent(
		val to: String,
		val content: NotificationContentData,
		val type: NotificationTypeData
)

class NotificationContentData(
		val title: String,
		val content: String
)

enum class NotificationTypeData {
	EMAIL,
	SMS;

	companion object {
		fun fromNotificationType(notificationType: NotificationType): NotificationTypeData {
			return when (notificationType) {
				NotificationType.EMAIL -> EMAIL
				NotificationType.SMS -> SMS
			}
		}
	}
}