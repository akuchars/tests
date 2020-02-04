package akuchars.notification.common.ui.rest.dto

class NotificationDataRestDto(
		val to: String,
		val title: String,
		val content: String,
		val type: NotificationRestType
)