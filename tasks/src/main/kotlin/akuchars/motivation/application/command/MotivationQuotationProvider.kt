package akuchars.motivation.application.command

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import akuchars.motivation.domain.model.MotivationAddressBook
import org.springframework.stereotype.Service

@Service
class MotivationQuotationProvider(
		private val motivationQuotationQueryServices: List<MotivationQuotationQueryService>
) {

	// todo akuchars przekazywać lepsze obiekty - z application
	fun getAllMotivationQuotation(addressBook: MotivationAddressBook): MotivationsQuotationDto {
		val sources = addressBook.configurationSources
		return motivationQuotationQueryServices
				.filter { sources.map { it.getType() }.any { typeFromSource -> it.sourceType() == typeFromSource } }
				.map { it.fetchMotivationsQuote(sources.find { source -> it.sourceType() == source.getType() }!!) }
				.flatMap { it.motivations }
				.let { MotivationsQuotationDto.copyFrom(it) }
	}

	fun getRandomlyMotivationQuotationForPerson(sources: MotivationAddressBook) = getAllMotivationQuotation(sources)
			.motivations.shuffled().first()
}