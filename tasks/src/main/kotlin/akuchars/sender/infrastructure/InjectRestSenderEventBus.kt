package akuchars.sender.infrastructure

import akuchars.notification.common.ui.rest.NotificationRestController
import akuchars.notification.common.ui.rest.dto.MultipleNotificationDataRestDto
import akuchars.notification.common.ui.rest.dto.NotificationDataRestDto
import akuchars.notification.common.ui.rest.dto.NotificationRestType
import akuchars.sender.domain.NotificationEvent
import akuchars.sender.domain.NotificationTypeData
import akuchars.sender.domain.NotificationTypeData.EMAIL
import akuchars.sender.domain.NotificationTypeData.SMS
import akuchars.sender.domain.SenderEventBus
import org.springframework.stereotype.Service

@Service
class InjectRestSenderEventBus(
		private val restController: NotificationRestController
) : SenderEventBus {

	// todo akuchars - dopisać prawdziwe uderzenie do API
	override fun sendNotifyEvent(event: NotificationEvent) {
		val restDto = MultipleNotificationDataRestDto(
				listOf(NotificationDataRestDto(event.to, event.content.title, event.content.content, event.type.toRestType()))
		)
		restController.notifyByType(restDto)
	}
}

private fun NotificationTypeData.toRestType(): NotificationRestType {
	return when (this) {
		EMAIL -> NotificationRestType.EMAIL
		SMS -> NotificationRestType.SMS
	}
}
