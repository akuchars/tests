package akuchars.motivation.domain

import akuchars.notification.common.domain.NotificationType

class MotivationAddressBook(
		val to: String,
		val type: NotificationType
)