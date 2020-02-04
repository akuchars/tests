package akuchars.notification.common.application.model

internal class NotificationDataDto(
		val to: String,
		val title: String,
		val content: String,
		val type: NotificationType
)