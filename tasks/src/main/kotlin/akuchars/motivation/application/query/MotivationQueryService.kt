package akuchars.motivation.application.query

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.EventBus
import akuchars.motivation.application.model.MotivationDataDto
import akuchars.motivation.domain.AddressBookRepository
import akuchars.motivation.domain.SourceQuotationRepository
import akuchars.motivation.domain.model.SourceQuotation
import akuchars.user.application.query.UserQueryService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MotivationQueryService(
		private val userQueryService: UserQueryService,
		private val addressBookRepository: AddressBookRepository,
		private val sourceQuotationRepository: SourceQuotationRepository,
		private val messageSource: MessageSource,
		private val frontDtoConverter: FrontDtoConverter,
		private val eventBus: EventBus
) {

	@Transactional(readOnly = true)
	fun finMotivationsForUser(pageable: Pageable): FrontDto<Page<MotivationDataDto>> {
		val email = userQueryService.getLoggedUser().contactData.email
		val addressBook = addressBookRepository.findByAddressee(email)
		return frontDtoConverter.toFrontDto {
			sourceQuotationRepository
					.findAllByAddressBook(pageable, addressBook)
					.map { it.toDto() }
		}
	}


	private fun SourceQuotation.toDto(): MotivationDataDto {
		val sourceMessage = messageSource.getMessage("motivation.source.${this.type}", arrayOf(), getLocale())
		return MotivationDataDto(motivation.id, motivation.text, motivation.author!!, active, sourceMessage)
	}

}

