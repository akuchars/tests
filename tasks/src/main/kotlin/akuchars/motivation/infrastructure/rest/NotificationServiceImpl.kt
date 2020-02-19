package akuchars.motivation.infrastructure.rest


import akuchars.common.kernel.logger
import akuchars.motivation.application.command.MotivationNotificationService
import akuchars.motivation.application.command.TextBuilder
import akuchars.motivation.domain.AddressBookRepository
import akuchars.motivation.domain.policy.MotivationChooserPolicy
import akuchars.sender.domain.SenderEventBus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationServiceImpl(
		private val addressBookRepository: AddressBookRepository,
		private val injectRestSenderEventBus: SenderEventBus,
		private val textBuilder: TextBuilder,
		private val motivationChooserPolicy: MotivationChooserPolicy
) : MotivationNotificationService {

	@Transactional
	override fun sendMotivationToInterested() {
		addressBookRepository.findAll().forEach {
			it.send(injectRestSenderEventBus, textBuilder, motivationChooserPolicy)
		}
		logger.info("Send motivation to all interested")
	}
}
