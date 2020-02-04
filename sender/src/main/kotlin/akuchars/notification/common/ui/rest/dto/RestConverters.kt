package akuchars.notification.common.ui.rest.dto

import akuchars.notification.common.application.model.MultipleNotificationDataDto
import akuchars.notification.common.application.model.NotificationDataDto
import akuchars.notification.common.application.model.NotificationType
import akuchars.notification.common.ui.rest.dto.NotificationRestType.EMAIL
import akuchars.notification.common.ui.rest.dto.NotificationRestType.SMS

internal fun NotificationDataRestDto.toDto() = NotificationDataDto(to, title, content, type.toDto())

internal fun NotificationRestType.toDto() = when (this) {
	EMAIL -> NotificationType.EMAIL
	SMS -> NotificationType.SMS
}

internal fun MultipleNotificationDataRestDto.toDto() = MultipleNotificationDataDto(notifications.map { it.toDto() })