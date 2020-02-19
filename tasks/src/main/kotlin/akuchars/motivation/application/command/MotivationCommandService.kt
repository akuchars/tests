package akuchars.motivation.application.command

import akuchars.common.domain.EventBus
import akuchars.motivation.application.model.CollectionOfMotivationQuatationsForm
import akuchars.motivation.domain.AddressBookRepository
import akuchars.motivation.domain.SourceQuotationRepository
import akuchars.motivation.domain.model.Quotation
import akuchars.motivation.domain.model.SourceQuotation
import akuchars.motivation.domain.model.SourceType.WWW
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class MotivationCommandService(
		private val addressBookRepository: AddressBookRepository,
		private val sourceQuotationRepository: SourceQuotationRepository,
		private val eventBus: EventBus
) {

	@Transactional
	fun createOrEdit(form: CollectionOfMotivationQuatationsForm) {
		val addressBook = addressBookRepository.findById(form.addressBookId)
				.orElseThrow(::EntityNotFoundException)
		val toEdit = form.motivations
				.filter { it.sourceMotivation != null }
				.map { it to sourceQuotationRepository.getById(it.sourceMotivation!!) }
				.map { it.second.modify(it.first.motivationText, it.first.author, it.first.active) }

		val toCreate = form.motivations
				.filter { it.sourceMotivation == null }
				.map {
					SourceQuotation(
							Quotation(it.motivationText, it.author),
							addressBook, it.active, WWW
					)
				}

		val updatedBook = addressBook.updateSource(eventBus, toEdit, toCreate)
		addressBookRepository.save(updatedBook)
	}
}