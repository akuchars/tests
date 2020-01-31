package akuchars.motivation.application.command

import akuchars.common.kernel.logger
import akuchars.motivation.application.model.MotivationQuotationDto
import akuchars.motivation.domain.MotivationAddressBookRepository
import akuchars.notification.common.application.NotificationServiceProvider
import akuchars.notification.common.application.model.NotificationData
import org.springframework.stereotype.Service

@Service
class MotivationNotificationService(
		private val motivationQuotationProvider: MotivationQuotationProvider,
		private val motivationAddressBookRepository: MotivationAddressBookRepository,
		private val notificationServiceProvider: NotificationServiceProvider
) {

	fun sendMotivationToInterested() {
		val motivationQuote = motivationQuotationProvider.getRandomlyMotivationQuotation()
		motivationAddressBookRepository.findAll().forEach {
			val notificationService = notificationServiceProvider.findNotificationServiceByType(it.type)
			val notificationToSend = NotificationData(it.to, "Zmotywuj się", motivationQuote.buildQuote())
			notificationService?.notify(notificationToSend)
			logger.info("Send ${motivationQuote.text} to: ${it.to}")
		}
	}

	private fun MotivationQuotationDto.buildQuote(): String = "$text \n $author"
}