package akuchars.motivation.infrastructure.rest


import akuchars.common.kernel.logger
import akuchars.motivation.application.command.MotivationNotificationService
import akuchars.motivation.application.command.MotivationTextBuilder
import akuchars.motivation.domain.MotivationAddressBookRepository
import akuchars.motivation.domain.policy.MotivationChooserPolicy
import akuchars.sender.domain.SenderEventBus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MotivationNotificationServiceImpl(
		private val motivationAddressBookRepository: MotivationAddressBookRepository,
		private val injectRestSenderEventBus: SenderEventBus,
		private val motivationTextBuilder: MotivationTextBuilder,
		private val motivationChooserPolicy: MotivationChooserPolicy
) : MotivationNotificationService {

	@Transactional
	override fun sendMotivationToInterested() {
		motivationAddressBookRepository.findAll().forEach {
			it.send(injectRestSenderEventBus, motivationTextBuilder, motivationChooserPolicy)
		}
		logger.info("Send motivation to all interested")
	}
}
