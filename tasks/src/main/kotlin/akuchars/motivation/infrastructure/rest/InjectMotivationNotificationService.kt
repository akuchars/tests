package akuchars.motivation.infrastructure.rest

//
//import akuchars.motivation.application.command.MotivationNotificationService
//import akuchars.motivation.application.command.MotivationQuotationProvider
//import akuchars.motivation.application.model.MotivationQuotationDto
//import akuchars.motivation.domain.MotivationAddressBookRepository
//import akuchars.motivation.domain.model.NotificationType
//import akuchars.motivation.domain.model.NotificationType.EMAIL
//import akuchars.motivation.domain.model.NotificationType.SMS
//import akuchars.notification.common.ui.rest.NotificationRestController
//import akuchars.notification.common.ui.rest.dto.MultipleNotificationDataRestDto
//import akuchars.notification.common.ui.rest.dto.NotificationDataRestDto
//import akuchars.notification.common.ui.rest.dto.NotificationRestType
//import org.springframework.stereotype.Service
//
//@Service
//// todo akuchars - dopisać prawdziwe uderzenie do API
//class InjectMotivationNotificationService(
//		private val motivationQuotationProvider: MotivationQuotationProvider,
//		private val motivationAddressBookRepository: MotivationAddressBookRepository,
//		private val restController: NotificationRestController
//) : MotivationNotificationService {
//
//	override fun sendMotivationToInterested() {
//		motivationAddressBookRepository.findAll().map {
//			val motivationQuote = motivationQuotationProvider.getRandomlyMotivationQuotationForPerson(it)
//			NotificationDataRestDto(it.to, "Zmotywuj się", motivationQuote.buildQuote(), it.type.toRestDto())
//		}.let(::MultipleNotificationDataRestDto).also(restController::notifyByType)
//	}
//
//	private fun MotivationQuotationDto.buildQuote(): String = "$text \n $author"
//
//	private fun NotificationType.toRestDto()= when(this) {
//		EMAIL -> NotificationRestType.EMAIL
//		SMS -> NotificationRestType.SMS
//	}
//}
