package akuchars.motivation.application.command

import akuchars.motivation.domain.model.NotificationType
import akuchars.sender.domain.NotificationContentData

interface TextBuilder {

	fun prepareMotivationText(motivationQuote: String, author: String?, type: NotificationType): NotificationContentData

}