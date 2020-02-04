package akuchars.notification.common.ui.rest

import akuchars.notification.common.application.NotificationServiceProvider
import akuchars.notification.common.ui.rest.dto.MultipleNotificationDataRestDto
import akuchars.notification.common.ui.rest.dto.toDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/notification"))
class NotificationRestController internal constructor(
		private val notificationServiceProvider: NotificationServiceProvider
) {

	@PostMapping("/send")
	fun notifyByType(@RequestBody notificationsDto: MultipleNotificationDataRestDto) {
		notificationServiceProvider.notifyMultiple(notificationsDto.toDto())
	}
}