package akuchars.motivation.application.query

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.EventBus
import akuchars.motivation.application.model.CollectionOfMotivationQuatationsForm
import akuchars.motivation.application.model.MotivationDataDto
import akuchars.motivation.domain.MotivationAddressBookRepository
import akuchars.motivation.domain.SourceMotivationQuotationRepository
import akuchars.motivation.domain.model.MotivationQuotation
import akuchars.motivation.domain.model.MotivationSourceType.WWW
import akuchars.motivation.domain.model.SourceMotivationQuotation
import akuchars.user.application.query.UserQueryService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class MotivationQueryService(
		private val userQueryService: UserQueryService,
		private val motivationAddressBookRepository: MotivationAddressBookRepository,
		private val sourceMotivationQuotationRepository: SourceMotivationQuotationRepository,
		private val messageSource: MessageSource,
		private val frontDtoConverter: FrontDtoConverter,
		private val eventBus: EventBus
) {

	@Transactional(readOnly = true)
	fun finMotivationsForUser(pageable: Pageable): FrontDto<Page<MotivationDataDto>> {
		val email = userQueryService.getLoggedUser().contactData.email
		val addressBook = motivationAddressBookRepository.findByAddressee(email)
		return frontDtoConverter.toFrontDto {
			sourceMotivationQuotationRepository
					.findAllByAddressBook(pageable, addressBook)
					.map { it.toDto() }
		}
	}

	@Transactional
	fun createOrEdit(form: CollectionOfMotivationQuatationsForm) {
		val addressBook = motivationAddressBookRepository.findById(form.addressBookId)
				.orElseThrow(::EntityNotFoundException)
		val toEdit = form.motivations
				.filter { it.sourceMotivation != null }
				.map { it to sourceMotivationQuotationRepository.getById(it.sourceMotivation!!) }
				.map { it.second.modify(it.first.motivationText, it.first.author, it.first.active) }

		val toCreate = form.motivations
				.filter { it.sourceMotivation == null }
				.map {
					SourceMotivationQuotation(
							MotivationQuotation(it.motivationText, it.author),
							addressBook, it.active, WWW
					)
				}

		val updatedBook = addressBook.updateSource(eventBus, toEdit, toCreate)
		motivationAddressBookRepository.save(updatedBook)
	}

	private fun SourceMotivationQuotation.toDto(): MotivationDataDto {
		val sourceMessage = messageSource.getMessage("motivation.source.${this.type}", arrayOf(), getLocale())
		return MotivationDataDto(motivation.id, motivation.text, motivation.author!!, active, sourceMessage)
	}

}

