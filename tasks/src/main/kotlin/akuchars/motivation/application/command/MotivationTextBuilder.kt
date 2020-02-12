package akuchars.motivation.application.command

import akuchars.motivation.domain.model.NotificationType
import akuchars.sender.domain.NotificationContentData

interface MotivationTextBuilder {

	fun prepareMotivationText(motivationQuote: String, author: String?, type: NotificationType): NotificationContentData

}