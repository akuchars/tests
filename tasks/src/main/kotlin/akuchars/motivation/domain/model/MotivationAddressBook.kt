package akuchars.motivation.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.domain.EventBus
import akuchars.common.kernel.ApplicationProperties
import akuchars.common.kernel.ApplicationProperties.MOTIVATIONS_QUEUE_NAME
import akuchars.motivation.application.command.MotivationTextBuilder
import akuchars.motivation.domain.UpdatedMotivationAddressBook
import akuchars.motivation.domain.policy.MotivationChooserPolicy
import akuchars.sender.domain.NotificationEvent
import akuchars.sender.domain.NotificationTypeData
import akuchars.sender.domain.SenderEventBus
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.MOTIVATION_SCHEMA_NAME, name = "address_books")
class MotivationAddressBook(
		// todo akuchars to może być jakiś fajny obiekt, a nie string
		val addressee: String,
		@Enumerated(EnumType.STRING)
		val type: NotificationType
) : AbstractJpaEntity() {
	@OneToMany(mappedBy = "addressBook", cascade = [CascadeType.ALL], orphanRemoval = true)
	lateinit var motivations: MutableList<SourceMotivationQuotation>

	fun send(senderEventBus: SenderEventBus,
	         motivationTextBuilder: MotivationTextBuilder,
	         motivationChooserPolicy: MotivationChooserPolicy) {
		val motivationContent = motivationChooserPolicy.chooseMotivationQuote(motivations.toList()).let {
			motivationTextBuilder.prepareMotivationText(it.text, it.author, type)
		}
		senderEventBus.sendNotifyEvent(NotificationEvent(
				addressee, motivationContent, NotificationTypeData.fromNotificationType(type)
		))
	}

	fun updateSource(eventBus: EventBus,
	                 toEdit: List<SourceMotivationQuotation>,
	                 toCreate: List<SourceMotivationQuotation>): MotivationAddressBook {
		motivations.removeIf { toEdit.any { toEditId -> it.id == toEditId.id } }
		motivations + toEdit
		motivations + toCreate
		eventBus.sendAsync(MOTIVATIONS_QUEUE_NAME, UpdatedMotivationAddressBook(id, addressee))
		return this
	}
}