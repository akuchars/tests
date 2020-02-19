package akuchars.motivation.application.command

import akuchars.motivation.domain.model.NotificationType
import akuchars.sender.domain.NotificationContentData
import org.springframework.stereotype.Component

@Component
class TextBuilderImpl : TextBuilder {

	//todo akuchars - teksty powinny być brane z messages.properties
	override fun prepareMotivationText(motivationQuote: String,
	                                   author: String?,
	                                   type: NotificationType
	): NotificationContentData = NotificationContentData(
			"Twój dzienny motywator",
			"'$motivationQuote' \n ~${author?.let { "Autor nieznany" }}"
	)
}